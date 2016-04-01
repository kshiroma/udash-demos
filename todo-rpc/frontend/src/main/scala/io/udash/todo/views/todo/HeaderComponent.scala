package io.udash.todo.views.todo

import io.udash._
import io.udash.i18n._
import io.udash.todo.Context
import io.udash.wrappers.jquery._
import io.udash.todo.i18n.Translations
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{html, _}

import scalatags.JsDom
import scalatags.JsDom.all._

object HeaderComponent {
  import Context._

  def apply(name: Property[String], addCallback: (Property[String]) => Any): JsDom.TypedTag[html.Element] =
    header(cls := "header")(
      TextInput(name)(
        translatedAttr(Translations.todos.input.placeholder(), "placeholder"),
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
