package io.udash.todo.i18n

import io.udash.i18n._

object Translations {
  import TranslationKey._

  object todos {
    object footer {
      val active = key("todos.footer.active")
      val all = key("todos.footer.all")
      val clear = key("todos.footer.clear")
      val completed = key("todos.footer.completed")
      val itemLeft = key1[Int]("todos.footer.itemLeft")
      val itemsLeft = key1[Int]("todos.footer.itemsLeft")
    }

    object input {
      val placeholder = key("todos.input.placeholder")
    }
  }
}
