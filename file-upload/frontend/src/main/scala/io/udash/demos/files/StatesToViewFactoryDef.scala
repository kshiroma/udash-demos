package io.udash.demos.files

import io.udash._
import io.udash.demos.files.views._
import io.udash.demos.files.views.index.IndexViewFactory

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] = state match {
    case RootState => RootViewFactory
    case IndexState => IndexViewFactory
    case _ => ErrorViewFactory
  }
}