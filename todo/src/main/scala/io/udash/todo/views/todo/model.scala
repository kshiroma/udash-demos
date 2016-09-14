package io.udash.todo.views.todo

case class Todo(name: String, editName: String = "", completed: Boolean = false, editing: Boolean = false)

sealed abstract class TodosFilter(val matcher: (Todo) => Boolean)
object TodosFilter {
  case object All extends TodosFilter(_ => true)
  case object Active extends TodosFilter(todo => !todo.completed)
  case object Completed extends TodosFilter(todo => todo.completed)
}

trait TodoViewModel {
  def todos: Seq[Todo]
  def todosFilter: TodosFilter

  def newTodoName: String
  def toggleAllChecked: Boolean
}