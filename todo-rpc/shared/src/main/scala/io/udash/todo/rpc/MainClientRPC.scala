package io.udash.todo.rpc

import io.udash.rpc.DefaultClientUdashRPCFramework
import io.udash.todo.rpc.model.Todo

trait MainClientRPC {
  def storeUpdated(todos: Seq[Todo]): Unit
}

object MainClientRPC extends DefaultClientUdashRPCFramework.RPCCompanion[MainClientRPC]