package io.udash.todo.rpc

import io.udash.i18n._
import io.udash.rpc._
import io.udash.todo.i18n.TodosTranslationTemplates
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

  override def i18n: RemoteTranslationRPC =
    new TranslationRPCEndpoint(TodosTranslationTemplates.provider)
}

       