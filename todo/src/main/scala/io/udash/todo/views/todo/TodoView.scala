package io.udash.todo.views.todo

import io.udash._
import io.udash.todo._
import io.udash.todo.storage.LocalTodoStorage
import io.udash.wrappers.jquery._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{Element, Event, KeyboardEvent}

object TodoViewPresenter extends ViewPresenter[TodoState] {
  import Context._

  override def create(): (View, Presenter[TodoState]) = {
    val model = ModelProperty[TodoViewModel]
    val presenter: TodoPresenter = new TodoPresenter(model)
    (new TodoView(model, presenter), presenter)
  }
}

class TodoPresenter(model: ModelProperty[TodoViewModel]) extends Presenter[TodoState] {
  private val todoList = model.subSeq(_.todoList)

  // Init model
  model.subProp(_.todoFilter).set(AllFilter)

  // Toggle button state update listener
  todoList.listen((s: Seq[TodoElement]) => {
    val targetValue = s.forall(_.completed)
    model.subProp(_.toggleAllChecked).set(targetValue)
  })

  // Persist to do list on every change
  todoList.listen(v => LocalTodoStorage.store(v.map(todo => storage.Todo(todo.name, todo.completed))))

  // Load from storage
  todoList.set(LocalTodoStorage.load().map(todo => Todo(todo.title, todo.completed, editing = false)))

  /** Sets list filter determined by active application state. */
  override def handleState(state: TodoState): Unit = {
    val filter = state match {
      case TodoAllState => AllFilter
      case TodoActiveState => ActiveFilter
      case TodoCompletedState => CompletedFilter
    }

    model.subProp(_.todoFilter).set(filter)
  }

  def addTodo(nameProperty: Property[String]): Unit = {
    val name = nameProperty.get.trim
    if (name.nonEmpty) {
      todoList.append(Todo(name, completed = false))
      nameProperty.set("")
    }
  }

  def startItemEdit(item: ModelProperty[TodoElement]): Unit = {
    item.subProp(_.editName).set(item.subProp(_.name).get)
    item.subProp(_.editing).set(true)
  }

  def cancelItemEdit(item: ModelProperty[TodoElement]): Unit =
    item.subProp(_.editing).set(false)

  def endItemEdit(item: ModelProperty[TodoElement]): Unit = {
    val name = item.subProp(_.editName).get.trim
    if (item.subProp(_.editing).get && name.nonEmpty) {
      item.subProp(_.name).set(name)
      item.subProp(_.editing).set(false)
    }
  }

  def deleteItem(item: ModelProperty[TodoElement]): Unit =
    todoList.remove(item.get)

  def clearCompleted(): Unit =
    todoList.set(todoList.get.filter(ActiveFilter.matcher))

  def setAllItemsCompleted(): Unit = {
    val targetValue = !model.subProp(_.toggleAllChecked).get
    CallbackSequencer.sequence {
      todoList.elemProperties.foreach(p => p.asModel.subProp(_.completed).set(targetValue))
    }
  }
}

class TodoView(model: ModelProperty[TodoViewModel], presenter: TodoPresenter) extends View {
  import Context._

  import scalatags.JsDom.all._
  import scalatags.JsDom.tags2._

  override def getTemplate: Element = content

  override def renderChild(view: View): Unit = {}

  private lazy val content = div(
    headerTemplate,
    listTemplate,
    footerTemplate
  ).render

  private lazy val headerTemplate =
    HeaderComponent(
      model.subProp(_.newTodoName),
      addCallback = presenter.addTodo
    )

  private lazy val listTemplate =
    TodoListComponent(
      model.subSeq(_.todoList),
      model.subProp(_.todoFilter),
      model.subProp(_.toggleAllChecked),
      presenter.setAllItemsCompleted,
      presenter.startItemEdit,
      presenter.cancelItemEdit,
      presenter.endItemEdit,
      presenter.deleteItem
    )

  private lazy val footerTemplate =
    FooterComponent(
      model.subSeq(_.todoList),
      model.subProp(_.todoFilter),
      clearCallback = presenter.clearCompleted
    )

  private def showIfTodoListIsNotEmpty(todos: ReadableSeqProperty[TodoElement]) =
    bindAttribute(todos)((todos: Seq[TodoElement], el: Element) => {
      if (todos.isEmpty) jQ(el).hide()
      else jQ(el).show()
    })
}
