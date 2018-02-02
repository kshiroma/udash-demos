package io.udash.todo.rpc

import io.udash.todo.rpc.model.Todo
import io.udash.todo.storage.TodoStorage

class RPCService(todoStorage: TodoStorage) extends MainClientRPC {
  override def storeUpdated(todos: Seq[Todo]): Unit =
    todoStorage.storeUpdated(todos)
}

       