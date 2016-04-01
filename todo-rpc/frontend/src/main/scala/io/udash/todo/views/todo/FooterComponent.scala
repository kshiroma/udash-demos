package io.udash.todo.views.todo

import io.udash._
import io.udash.i18n._
import io.udash.todo.i18n.Translations
import io.udash.todo.{Context, TodoActiveState, TodoAllState, TodoCompletedState}
import io.udash.wrappers.jquery._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{html, _}

import scalatags.JsDom
import scalatags.JsDom.all._

object FooterComponent {
  import Context._

  def apply(todoList: ReadableSeqProperty[TodoElement],
            filter: Property[TodoListFilter],
            clearCallback: () => Any): JsDom.TypedTag[html.Element] =
    footer(
      cls := "footer",
      showIfListIsNotEmpty(todoList)
    )(
      produce(todoList.filter(ActiveFilter.matcher))(todos => {
        val size: Int = todos.size
        span(cls := "todo-count")(
          translated((size match {
            case 1 => Translations.todos.footer.itemLeft
            case _ => Translations.todos.footer.itemsLeft
          })(size))
        ).render
      }),
      ul(cls := "filters")(
        li(
          a(
            href := TodoAllState.url,
            bindAttribute(filter)((v: TodoListFilter, el: Element) =>
              updateLinkState(el, AllFilter, v)
            )
          )(translated(Translations.todos.footer.all()))
        ),
        li(
          a(
            href := TodoActiveState.url,
            bindAttribute(filter)((v: TodoListFilter, el: Element) =>
              updateLinkState(el, ActiveFilter, v)
            )
          )(translated(Translations.todos.footer.active()))
        ),
        li(
          a(
            href := TodoCompletedState.url,
            bindAttribute(filter)((v: TodoListFilter, el: Element) =>
              updateLinkState(el, CompletedFilter, v)
            )
          )(translated(Translations.todos.footer.completed()))
        )
      ),
      button(
        cls := "clear-completed",
        showIfListIsNotEmpty(todoList.filter(CompletedFilter.matcher)),
        onclick :+= ((ev: Event) => { clearCallback(); true })
      )(translated(Translations.todos.footer.clear()))
    )

  private def updateLinkState(el: Element, expectedFilter: TodoListFilter, activeFilter: TodoListFilter): Unit =
    if (activeFilter == expectedFilter) jQ(el).addClass("selected")
    else jQ(el).removeClass("selected")

  private def showIfListIsNotEmpty(todos: ReadableSeqProperty[_]) =
    bindAttribute(todos)((list: Seq[_], el: Element) => {
      if (list.isEmpty) jQ(el).hide()
      else jQ(el).show()
    })
}
