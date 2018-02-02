package io.udash.todo.rpc.model

import com.avsystem.commons.serialization.HasGenCodec

case class Todo(title: String, completed: Boolean)
object Todo extends HasGenCodec[Todo]
