package io.udash.todo.rpc

import com.avsystem.commons.rpc.RPC
import io.udash.todo.rpc.model.Todo

import scala.concurrent.Future

@RPC
trait MainServerRPC {
  def store(todos: Seq[Todo]): Future[Boolean]
  def load(): Future[Seq[Todo]]
}

       