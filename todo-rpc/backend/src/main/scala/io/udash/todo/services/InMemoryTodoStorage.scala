package io.udash.todo.services

import io.udash.todo.rpc.model.Todo

object InMemoryTodoStorage extends TodoStorage {
  private var storage: Seq[Todo] = Seq.empty[Todo]

  override def store(todo: Seq[Todo]): Boolean = synchronized {
    if (storage != todo) {
      storage = todo
      true
    } else false
  }

  override def load(): Seq[Todo] = synchronized {
    storage
  }
}
