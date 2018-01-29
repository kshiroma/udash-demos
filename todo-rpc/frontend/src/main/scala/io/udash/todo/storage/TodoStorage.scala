package io.udash.todo.storage

import io.udash.todo.rpc.model.Todo
import io.udash.utils.Registration

import scala.concurrent.Future

trait TodoStorage {
  def store(todo: Seq[Todo]): Unit
  def load(): Future[Seq[Todo]]
  def storeUpdated(todoList: Seq[Todo]): Unit
  def listen(l: Seq[Todo] => Any): Registration
}