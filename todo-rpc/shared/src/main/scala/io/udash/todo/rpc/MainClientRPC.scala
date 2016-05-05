package io.udash.todo.rpc

import com.avsystem.commons.rpc.RPC
import io.udash.todo.rpc.model.Todo

@RPC
trait MainClientRPC {
  def storeUpdated(todos: Seq[Todo]): Unit
}

       