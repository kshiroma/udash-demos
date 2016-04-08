package io.udash.todo.rpc

import io.udash.todo.rpc.model.Todo
import io.udash.todo.storage.RemoteTodoStorage

class RPCService extends MainClientRPC {
  override def storeUpdated(todos: Seq[Todo]): Unit =
    RemoteTodoStorage.storeUpdated(todos)
}

       