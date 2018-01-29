package io.udash.todo

import io.udash.Application
import io.udash.rpc.DefaultServerRPC
import io.udash.todo.rpc.{MainClientRPC, MainServerRPC, RPCService}
import io.udash.todo.storage.{RemoteTodoStorage, TodoStorage}

object ApplicationContext {
  val todoStorage: TodoStorage = new RemoteTodoStorage

  private val routingRegistry = new RoutingRegistryDef
  private val viewFactoriesRegistry = new StatesToViewFactoryDef

  val applicationInstance: Application[RoutingState] = new Application[RoutingState](routingRegistry, viewFactoriesRegistry)
  val serverRpc: MainServerRPC = DefaultServerRPC[MainClientRPC, MainServerRPC](new RPCService(todoStorage))
}
