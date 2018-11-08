package jp.co.camnet.dekitayo

import io.udash._
import jp.co.camnet.dekitayo.view.dekitayo.DekitayoFilter

class DekitayoRoutingState(val parentState: Option[DekitayoContainerRoutingState]) extends State {

  type HierarchyRoot = DekitayoRoutingState

  def url(implicit application: Application[DekitayoRoutingState]): String = s"#${application.matchState(this).value}"
}


sealed abstract class DekitayoContainerRoutingState(parentState: Option[DekitayoContainerRoutingState])
  extends DekitayoRoutingState(parentState) with ContainerState

sealed abstract class DekitayoFinalRoutingState(parentState: Option[DekitayoContainerRoutingState])
  extends DekitayoRoutingState(parentState)
    with FinalState

object DekitayoRootState extends DekitayoContainerRoutingState(None)

object DekitayoErrorState extends DekitayoFinalRoutingState(Some(DekitayoRootState))

case class DekitayoState(filter: DekitayoFilter) extends DekitayoFinalRoutingState(Some(DekitayoRootState))