package io.udash.todo.services

import io.udash.todo.rpc.model.Todo

trait TodoStorage {
  def store(todo: Seq[Todo]): Boolean
  def load(): Seq[Todo]
}