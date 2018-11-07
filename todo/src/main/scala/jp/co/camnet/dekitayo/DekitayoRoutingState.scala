package jp.co.camnet.dekitayo

import io.udash.Application
import io.udash.core.{ContainerState, FinalState, State}


sealed abstract class DekitayoRoutingState(val parentState: Option[DekitayoContainerRoutingState]) extends State {
  type HierarachyRoot = DekitayoRoutingState

  def url(implicit application: Application[DekitayoRoutingState]): String = s"#${application.matchState(this).value}"

}

sealed abstract class DekitayoContainerRoutingState(parentState: Option[DekitayoContainerRoutingState])
  extends DekitayoRoutingState(parentState) with ContainerState

sealed abstract class DekitayoFinalRoutingState(parentState: Option[DekitayoContainerRoutingState])
  extends DekitayoRoutingState(parentState) with FinalState


object DekitayoRootState
 extends  DekitayoContainerRoutingState(None)

object DekitayoErrorState extends DekitayoFinalRoutingState(Some(DekitayoRootState))

object DekitayoState(filter:DekitayoFilter) extends DekitayoFinalRoutingState(Some(DekitayoRootState))


