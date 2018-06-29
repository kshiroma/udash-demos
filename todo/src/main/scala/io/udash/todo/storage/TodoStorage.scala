package io.udash.todo.storage

import com.avsystem.commons.serialization.HasGenCodec
import com.avsystem.commons.serialization.json.{JsonStringInput, JsonStringOutput}
import org.scalajs.dom.ext.LocalStorage

case class Todo(title: String, completed: Boolean)
object Todo extends HasGenCodec[Todo]

trait TodoStorage {
  def store(todo: Seq[Todo]): Unit
  def load(): Seq[Todo]
}

object LocalTodoStorage extends TodoStorage {
  private val storage = LocalStorage
  private val namespace = "todos-udash"

  override def store(todos: Seq[Todo]): Unit =
    storage(namespace) = JsonStringOutput.write(todos)

  def load(): Seq[Todo] = {
    storage(namespace) match {
      case Some(todos) =>
        JsonStringInput.read[Seq[Todo]](todos)
      case None =>
        Seq.empty
    }
  }
}