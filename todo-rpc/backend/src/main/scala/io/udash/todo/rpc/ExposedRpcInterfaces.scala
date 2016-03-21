package io.udash.todo.rpc

import io.udash.rpc._
import io.udash.todo.rpc.model.Todo
import io.udash.todo.services.TodoStorage

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ExposedRpcInterfaces(todoStorage: TodoStorage)(implicit clientId: ClientId) extends MainServerRPC {
  override def store(todos: Seq[Todo]): Future[Boolean] = Future {
    if (todoStorage.store(todos)) {
      ClientRPC(AllClients).storeUpdated(todos)
      true
    } else false
  }

  override def load(): Future[Seq[Todo]] = Future {
    todoStorage.load()
  }
}

       