package io.udash.demos.files

import io.udash.Application
import io.udash.demos.files.rpc.{MainClientRPC, MainServerRPC, RPCService}

object ApplicationContext {
  private val routingRegistry = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewFactoryDef

  val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry)

  import io.udash.rpc._
  val rpcService: RPCService = new RPCService
  val serverRpc = DefaultServerRPC[MainClientRPC, MainServerRPC](rpcService)
}
