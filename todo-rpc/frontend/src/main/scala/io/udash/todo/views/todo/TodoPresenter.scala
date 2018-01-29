package io.udash.todo.views.todo

import io.udash._
import io.udash.logging.CrossLogging
import io.udash.todo._
import io.udash.todo.rpc.model.{Todo => STodo}
import io.udash.todo.storage.TodoStorage

import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

class TodoPresenter(model: ModelProperty[TodoViewModel], todoStorage: TodoStorage)
  extends Presenter[TodoState] with CrossLogging {

  private val todos = model.subSeq(_.todos)

  // Toggle button state update listener
  private val toggleButtonListener = todos.listen { todos =>
    model.subProp(_.toggleAllChecked).set(todos.forall(_.completed))
  }

  // Load from storage
  todoStorage.load() onComplete {
    case Success(response) =>
      updateTodos(response)

      // Persist to do list on every change
      todos.listen { v =>
        todoStorage.store(v.map(todo => STodo(todo.name, todo.completed)))
      }
    case Failure(ex) =>
      logger.error("Can not load todos from server!")
  }

  // Persist todos on every change
  private val todosPersistListener = todoStorage.listen { todos =>
    updateTodos(todos)
  }

  override def handleState(state: TodoState): Unit = {
    model.subProp(_.todosFilter).set(state.filter)
  }

  override def onClose(): Unit = {
    super.onClose()
    toggleButtonListener.cancel()
    todosPersistListener.cancel()
  }

  def addTodo(): Unit = {
    val nameProperty: Property[String] = model.subProp(_.newTodoName)
    val name = nameProperty.get.trim
    if (name.nonEmpty) {
      todos.append(Todo(name))
      nameProperty.set("")
    }
  }

  def startItemEdit(item: ModelProperty[Todo], nameEditor: Property[String]): Unit = {
    nameEditor.set(item.subProp(_.name).get)
    item.subProp(_.editing).set(true)
  }

  def cancelItemEdit(item: ModelProperty[Todo]): Unit =
    item.subProp(_.editing).set(false)

  def endItemEdit(item: ModelProperty[Todo], nameEditor: Property[String]): Unit = {
    val name = nameEditor.get.trim
    if (item.subProp(_.editing).get && name.nonEmpty) {
      item.subProp(_.name).set(name)
      item.subProp(_.editing).set(false)
    } else if (name.isEmpty) {
      deleteItem(item.get)
    }
  }

  def deleteItem(item: Todo): Unit =
    todos.remove(item)

  def clearCompleted(): Unit =
    todos.set(todos.get.filter(TodosFilter.Active.matcher))

  def setItemsCompleted(): Unit =
    CallbackSequencer().sequence {
      val targetValue = !model.subProp(_.toggleAllChecked).get
      todos.elemProperties.foreach(p => p.asModel.subProp(_.completed).set(targetValue))
    }

  private def updateTodos(updated: Seq[STodo]): Unit =
    todos.set(updated.map(todo => Todo(name = todo.title, completed = todo.completed)))
}
