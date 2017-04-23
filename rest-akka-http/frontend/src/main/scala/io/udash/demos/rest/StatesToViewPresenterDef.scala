package io.udash.demos.rest

import io.udash._
import io.udash.demos.rest.views._
import io.udash.demos.rest.views.book.PhoneBookFormViewPresenter
import io.udash.demos.rest.views.contact.ContactFormViewPresenter
import io.udash.demos.rest.views.index.IndexViewPresenter

class StatesToViewPresenterDef extends ViewPresenterRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewPresenter[_ <: RoutingState] = state match {
    case RootState => RootViewPresenter
    case IndexState => IndexViewPresenter
    case ContactFormState(urlArg) => ContactFormViewPresenter(urlArg)
    case PhoneBookFormState(urlArg) => PhoneBookFormViewPresenter(urlArg)
    case _ => ErrorViewPresenter
  }
}