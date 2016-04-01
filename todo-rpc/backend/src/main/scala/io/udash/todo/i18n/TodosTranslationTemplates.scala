package io.udash.todo.i18n

import java.{util => ju}

import io.udash.i18n._

object TodosTranslationTemplates {
  val langs = Seq("en", "pl")
  val bundles = Seq("todos")

  val provider = new ResourceBundlesTranslationTemplatesProvider(
    langs.map(lang =>
      Lang(lang) -> bundles.map(name => ju.ResourceBundle.getBundle(name, new ju.Locale(lang)))
    ).toMap
  )
}
