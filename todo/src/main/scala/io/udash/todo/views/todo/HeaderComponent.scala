package io.udash.todo.views.todo

import io.udash._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{html, _}

import scalatags.JsDom
import scalatags.JsDom.all._

object HeaderComponent {
  def apply(name: Property[String], addCallback: (Property[String]) => Any): JsDom.TypedTag[html.Element] =
    header(cls := "header")(
      TextInput.debounced(name)(
        cls := "new-todo",
        placeholder :=  "What needs to be done?",
        autofocus :=  true,
        onkeydown :+= ((ev: KeyboardEvent) => {
          if (ev.keyCode == KeyCode.Enter) {
            addCallback(name)
            true //prevent default
          } else false // do not prevent default
        })
      )
    )
}
