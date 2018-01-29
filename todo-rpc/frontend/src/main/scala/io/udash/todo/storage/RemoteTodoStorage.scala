package io.udash.todo.storage

import io.udash.todo.rpc.model.Todo
import io.udash.utils.{CallbacksHandler, Registration}

import scala.concurrent.Future

class RemoteTodoStorage extends TodoStorage {
  import io.udash.todo.ApplicationContext._

  private val listeners = new CallbacksHandler[Seq[Todo]]

  override def store(todoList: Seq[Todo]): Unit =
    serverRpc.store(todoList)

  override def load(): Future[Seq[Todo]] =
    serverRpc.load()

  override def storeUpdated(todoList: Seq[Todo]): Unit =
    listeners.fire(todoList)

  override def listen(l: Seq[Todo] => Any): Registration = {
    listeners.register(PartialFunction(l))
  }
}