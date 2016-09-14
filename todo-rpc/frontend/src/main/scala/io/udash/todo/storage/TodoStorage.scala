package io.udash.todo.storage

import io.udash.todo.Context
import io.udash.todo.rpc.model.Todo
import org.scalajs.dom.ext.LocalStorage

import scala.collection.mutable
import scala.concurrent.Future

trait TodoStorage {
  def store(todo: Seq[Todo]): Unit
  def load(): Future[Seq[Todo]]
}

object RemoteTodoStorage extends TodoStorage {
  import Context._

  private val listeners = mutable.ArrayBuffer[(Seq[Todo]) => Any]()

  override def store(todoList: Seq[Todo]): Unit =
    serverRpc.store(todoList)

  override def load(): Future[Seq[Todo]] =
    serverRpc.load()

  def storeUpdated(todoList: Seq[Todo]): Unit =
    listeners.foreach(l => l(todoList))

  def listen(l: (Seq[Todo]) => Any): Unit = {
    listeners += l
  }
}