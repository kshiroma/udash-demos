package io.udash.todo

import io.udash._
import io.udash.utils.Bidirectional

class RoutingRegistryDef extends RoutingRegistry[RoutingState] {
  def matchUrl(url: Url): RoutingState = {
    val value: String = url.value

    import io.udash.i18n._
    val lang = if ((value.length >= 4 && value.charAt(3) == '/') || value.length == 3) value.substring(1,3) else "en"
    Context.langProperty.set(Lang(lang))

    url2State.applyOrElse(value.substring(3).stripSuffix("/"), (x: String) => ErrorState)
  }

  def matchState(state: RoutingState): Url =
    Url(s"/${Context.langProperty.get.lang}" + state2Url.apply(state))

  private val (url2State, state2Url) = Bidirectional[String, RoutingState] {
    case "" => TodoAllState
    case "/active" => TodoActiveState
    case "/completed" => TodoCompletedState
  }
}
