package io.udash.todo.views.todo

sealed abstract class TodoListFilter(val matcher: (TodoElement) => Boolean)
case object AllFilter       extends TodoListFilter(_ => true)
case object ActiveFilter    extends TodoListFilter(todo => !todo.completed)
case object CompletedFilter extends TodoListFilter(todo => todo.completed)

/** Property Todo model. */
trait TodoElement {
  def name: String
  def editName: String
  def completed: Boolean
  def editing: Boolean
}

/** View state. */
trait TodoViewModel {
  def todoList: Seq[TodoElement]
  def todoFilter: TodoListFilter

  def newTodoName: String
  def toggleAllChecked: Boolean
}

case class Todo(override val name: String,
                override val completed: Boolean,
                override val editName: String = "",
                override val editing: Boolean = false) extends TodoElement