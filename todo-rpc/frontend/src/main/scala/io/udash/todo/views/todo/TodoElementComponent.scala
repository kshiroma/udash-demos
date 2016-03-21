package io.udash.todo.views.todo

import io.udash._
import io.udash.todo.{Context, TodoActiveState, TodoAllState, TodoCompletedState}
import io.udash.wrappers.jquery._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{html, _}

import scalatags.JsDom
import scalatags.JsDom.all._

object TodoElementComponent {
  import Context._

  def apply(item: ModelProperty[TodoElement],
            startItemEdit: () => Any,
            cancelItemEdit: () => Any,
            endItemEdit: () => Any,
            deleteItem: () => Any): JsDom.TypedTag[html.Element] =
    li(
      bindAttribute(item)((item: TodoElement, el: Element) => {
        val jEl: JQuery = jQ(el)

        if (item.completed) jEl.addClass("completed")
        else jEl.removeClass("completed")

        if (item.editing) {
          jEl.addClass("editing")
          jEl.children(".edit").focus()
        } else jEl.removeClass("editing")
      })
    )(
      div(cls := "view")(
        Checkbox(item.subProp(_.completed), cls := "toggle"),
        label(
          ondblclick :+= ((ev: Event) => { startItemEdit(); true })
        )(bind(item.subProp(_.name))),
        button(
          cls := "destroy",
          onclick :+= ((ev: Event) => { deleteItem(); true })
        )
      ),
      TextInput(item.subProp(_.editName))(
        cls := "edit",
        onkeydown :+= ((ev: KeyboardEvent) => {
          if (ev.keyCode == KeyCode.Enter) {
            endItemEdit()
            true //prevent default
          } else if (ev.keyCode == KeyCode.Escape) {
            cancelItemEdit()
            true //prevent default
          } else false // do not prevent default
        }),
        onblur :+= ((ev: Event) => {
          endItemEdit()
          true //prevent default
        })
      )
    )
}
