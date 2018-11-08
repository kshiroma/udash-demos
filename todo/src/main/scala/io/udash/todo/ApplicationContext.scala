package io.udash.todo

import io.udash.Application
import jp.co.camnet.dekitayo.{DekitayoRoutingRegistryDef, DekitayoRoutingState, DekitayoStateToViewFactoryDef}

object ApplicationContext {
  private val routingRegistry = new RoutingRegistryDef
  private val viewFactoriesRegistry = new StatesToViewFactoryDef

  private val dekitayoRoutingRegistry = new DekitayoRoutingRegistryDef
  private val dekitayoViewFactoriesRegistry = new DekitayoStateToViewFactoryDef

  val applicationInstance: Application[RoutingState] = new Application[RoutingState](routingRegistry, viewFactoriesRegistry)


  val dekitayoApplicatonInstance: Application[DekitayoRoutingState] = new Application[DekitayoRoutingState](dekitayoRoutingRegistry, dekitayoViewFactoriesRegistry)
}
