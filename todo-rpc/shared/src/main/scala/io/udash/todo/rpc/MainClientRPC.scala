package io.udash.todo.rpc

import io.udash.rpc._
import io.udash.todo.rpc.model.Todo

trait MainClientRPC extends ClientRPC {
  def storeUpdated(todos: Seq[Todo]): Unit
}

       