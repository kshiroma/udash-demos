package jp.co.camnet.dekitayo

import io.udash._
import jp.co.camnet.dekitayo.view.dekitayo.DekitayoFilter

class DekitayoRoutingRegistryDef extends RoutingRegistry[DekitayoRoutingState] {
  def matchUrl(url: Url): DekitayoRoutingState = url2State.applyOrElse(url.value.stripSuffix("/"), (x: String) => DekitayoErrorState)

  def matchState(state: DekitayoRoutingState): Url = {
    Url(state2Url.apply(state))
  }

  private val (url2State, state2Url) = bidirectional {
    case "" => DekitayoState(DekitayoFilter.All)
    case "/active" => DekitayoState(DekitayoFilter.Active)
    case "/completed" => DekitayoState(DekitayoFilter.Completed)
  }


}
