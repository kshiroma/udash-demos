package jp.co.camnet.dekitayo

import io.udash._
import jp.co.camnet.dekitayo.view.dekitayo.DekitayoViewFactory
import jp.co.camnet.dekitayo.view.{DekitayoErrorView, DekitayoRootViewFactory}

class DekitayoStateToViewFactoryDef extends ViewFactoryRegistry[DekitayoRoutingState] {

  def matchStateToResolver(state: DekitayoRoutingState): ViewFactory[_ <: DekitayoRoutingState] = state match {
    case DekitayoRootState => DekitayoRootViewFactory
    case _: DekitayoState => DekitayoViewFactory
    case _ => DekitayoErrorView
  }


}
