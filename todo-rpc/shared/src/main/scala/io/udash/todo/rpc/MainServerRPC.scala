package io.udash.todo.rpc

import io.udash.rpc.DefaultServerUdashRPCFramework
import io.udash.todo.rpc.model.Todo

import scala.concurrent.Future

trait MainServerRPC {
  def store(todos: Seq[Todo]): Future[Boolean]
  def load(): Future[Seq[Todo]]
}

object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]