package io.udash.demos.rest.views.contact

import io.udash._
import io.udash.core.Presenter
import io.udash.demos.rest.model.ContactId
import io.udash.demos.rest.{ContactFormState, Context}

case class ContactFormViewPresenter(id: Option[ContactId]) extends ViewPresenter[ContactFormState] {
  override def create(): (View, Presenter[ContactFormState]) = {
    import Context._
    val model = ModelProperty[ContactEditorModel]
    val presenter = new ContactFormPresenter(model)
    val view = new ContactFormView(model, presenter)
    (view, presenter)
  }
}
