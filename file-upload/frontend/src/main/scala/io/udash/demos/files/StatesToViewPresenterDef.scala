package io.udash.demos.files

import io.udash._
import io.udash.demos.files.views._
import io.udash.demos.files.views.index.IndexViewPresenter

class StatesToViewPresenterDef extends ViewPresenterRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewPresenter[_ <: RoutingState] = state match {
    case RootState => RootViewPresenter
    case IndexState => IndexViewPresenter
    case _ => ErrorViewPresenter
  }
}