package io.udash.todo.views.todo

import io.udash._
import io.udash.todo._
import io.udash.todo.storage.RemoteTodoStorage
import io.udash.utils.StrictLogging
import io.udash.wrappers.jquery._
import org.scalajs.dom.Element

import scala.concurrent.Future
import scala.util.{Failure, Success}

import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{Element, Event, KeyboardEvent}

object TodoViewPresenter extends ViewPresenter[TodoState] {
  import Context._

  override def create(): (View, Presenter[TodoState]) = {
    val model = ModelProperty[TodoViewModel]
    val presenter: TodoPresenter = new TodoPresenter(model)
    (new TodoView(model, presenter), presenter)
  }
}

class TodoPresenter(model: ModelProperty[TodoViewModel]) extends Presenter[TodoState] with StrictLogging {
  import Context._
  import io.udash.todo.rpc.model.{Todo => STodo}

  private val todos = model.subSeq(_.todos)
  private var updateFromServer = false

  // Init model with AllTodosFilter filter
  model.subProp(_.todosFilter).set(TodosFilter.All)

  // Toggle button state update listener
  todos.listen((s: Seq[Todo]) => model.subProp(_.toggleAllChecked).set(s.forall(_.completed)))

  // Load from storage
  RemoteTodoStorage.load() onComplete {
    case Success(response) =>
      updateTodos(response)

      // Persist to do list on every change
      todos.listen(v => if (!updateFromServer)
        RemoteTodoStorage.store(v.map(todo => STodo(todo.name, todo.completed)))
      )
    case Failure(ex) =>
      logger.error("Can not load todos from server!")
  }

  // Persist todos on every change
  RemoteTodoStorage.listen((todos: Seq[STodo]) => {
    updateFromServer = true
    updateTodos(todos)
    updateFromServer = false
  })

  private def updateTodos(updated: Seq[STodo]): Unit =
    todos.set(updated.map(todo => Todo(name = todo.title, completed = todo.completed, editing = false)))

  override def handleState(state: TodoState): Unit =
    model.subProp(_.todosFilter).set(state match {
      case TodoAllState => TodosFilter.All
      case TodoActiveState => TodosFilter.Active
      case TodoCompletedState => TodosFilter.Completed
    })

  def addTodo(): Unit = {
    val nameProperty: Property[String] = model.subProp(_.newTodoName)
    val name = nameProperty.get.trim
    if (name.nonEmpty) {
      todos.append(Todo(name, completed = false))
      nameProperty.set("")
    }
  }

  def startItemEdit(item: ModelProperty[Todo]): Unit = {
    item.subProp(_.editName).set(item.subProp(_.name).get)
    item.subProp(_.editing).set(true)
  }

  def cancelItemEdit(item: ModelProperty[Todo]): Unit =
    item.subProp(_.editing).set(false)

  def endItemEdit(item: ModelProperty[Todo]): Unit = {
    val name = item.subProp(_.editName).get.trim
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
    CallbackSequencer.sequence {
      val targetValue = !model.subProp(_.toggleAllChecked).get
      todos.elemProperties.foreach(p => p.asModel.subProp(_.completed).set(targetValue))
    }
}

class TodoView(model: ModelProperty[TodoViewModel],
               presenter: TodoPresenter) extends FinalView {
  import Context._

  import scalatags.JsDom.all._
  import scalatags.JsDom.tags2.section

  private val isTodoListNonEmpty: ReadableProperty[Boolean] =
    model.subSeq(_.todos).transform(_.nonEmpty)
  private val isCompletedTodoListNonEmpty: ReadableProperty[Boolean] =
    model.subSeq(_.todos).filter(TodosFilter.Completed.matcher).transform(_.nonEmpty)

  override val getTemplate: Modifier = div(
    headerTemplate,
    showIf(isTodoListNonEmpty)(Seq(
      listTemplate.render,
      footerTemplate.render
    ))
  )

  private lazy val headerTemplate =
    header(cls := "header")(
      TextInput(model.subProp(_.newTodoName), debounce = None)(
        cls := "new-todo",
        placeholder :=  "What needs to be done?",
        autofocus :=  true,
        onkeydown :+= ((ev: KeyboardEvent) => {
          if (ev.keyCode == KeyCode.Enter) {
            presenter.addTodo()
            true //prevent default
          } else false // do not prevent default
        })
      )
    )

  private lazy val listTemplate =
    section(cls := "main")(
      Checkbox(
        model.subProp(_.toggleAllChecked),
        cls := "toggle-all",
        onclick :+= ((ev: Event) => { presenter.setItemsCompleted(); false })
      ),
      produce(model.subProp(_.todosFilter))(filter =>
        ul(cls := "todo-list")(
          repeat(model.subSeq(_.todos).filter(filter.matcher))(
            (item: CastableProperty[Todo]) =>
              listItemTemplate(item.asModel).render
          )
        ).render
      )
    )

  private def listItemTemplate(item: ModelProperty[Todo]) =
    li(
      reactiveClass(item.subProp(_.completed), "completed"),
      reactiveClass(item.subProp(_.editing), "editing")
    )(
      div(cls := "view")(
        Checkbox(item.subProp(_.completed), cls := "toggle"),
        label(
          ondblclick :+= ((ev: Event) => presenter.startItemEdit(item), true)
        )(bind(item.subProp(_.name))),
        button(
          cls := "destroy",
          onclick :+= ((ev: Event) => presenter.deleteItem(item.get), true)
        )
      ),
      TextInput(item.subProp(_.editName), debounce = None)(
        item.subProp(_.editing).reactiveApply((el, editing) =>
          if (editing) Future { el.asInstanceOf[HTMLElement].focus() }
        ),
        cls := "edit", tabindex := -1,
        onkeydown :+= ((ev: KeyboardEvent) => {
          if (ev.keyCode == KeyCode.Enter) {
            presenter.endItemEdit(item)
            true //prevent default
          } else if (ev.keyCode == KeyCode.Escape) {
            presenter.cancelItemEdit(item)
            true //prevent default
          } else false // do not prevent default
        }),
        onblur :+= ((ev: Event) => {
          presenter.endItemEdit(item)
          true //prevent default
        })
      )
    )

  private def footerButtonTemplate(title: String, link: String, expectedFilter: TodosFilter) =
    a(
      href := link,
      reactiveClass(model.subProp(_.todosFilter).transform(_ == expectedFilter), "selected")
    )(title)

  private lazy val footerTemplate =
    footer(cls := "footer")(
      produce(model.subSeq(_.todos).filter(TodosFilter.Active.matcher))(todos => {
        val size: Int = todos.size
        val pluralization = if (size == 1) "item" else "items"
        span(cls := "todo-count")(s"$size $pluralization left").render
      }),
      ul(cls := "filters")(
        li(footerButtonTemplate("All", TodoAllState.url, TodosFilter.All)),
        li(footerButtonTemplate("Active", TodoActiveState.url, TodosFilter.Active)),
        li(footerButtonTemplate("Completed", TodoCompletedState.url, TodosFilter.Completed))
      ),
      showIf(isCompletedTodoListNonEmpty)(Seq(
        button(
          cls := "clear-completed",
          onclick :+= ((ev: Event) => presenter.clearCompleted(), true)
        )("Clear completed").render
      )
      ))

  private def reactiveClass(p: ReadableProperty[Boolean], clsName: String) = new Modifier {
    private def updater(el: Element, add: Boolean) =
      if (add) el.classList.add(clsName)
      else el.classList.remove(clsName)

    override def applyTo(t: Element): Unit =
      p.reactiveApply(updater).applyTo(t)
  }
}
