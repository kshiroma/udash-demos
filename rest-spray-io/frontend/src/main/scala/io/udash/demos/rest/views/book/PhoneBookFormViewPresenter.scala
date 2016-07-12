package io.udash.demos.rest.views.book

import io.udash._
import io.udash.core.Presenter
import io.udash.demos.rest.model.PhoneBookId
import io.udash.demos.rest.{Context, PhoneBookFormState}

case class PhoneBookFormViewPresenter(id: Option[PhoneBookId]) extends ViewPresenter[PhoneBookFormState] {
  override def create(): (View, Presenter[PhoneBookFormState]) = {
    import Context._
    val model = ModelProperty[PhoneBookEditorModel]
    val presenter = new PhoneBookFormPresenter(model)
    val view = new PhoneBookFormView(model, presenter)
    (view, presenter)
  }
}
