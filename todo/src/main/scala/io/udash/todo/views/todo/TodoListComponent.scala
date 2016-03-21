package io.udash.todo.views.todo

import io.udash._
import io.udash.todo.{Context, TodoActiveState, TodoAllState, TodoCompletedState}
import io.udash.wrappers.jquery._
import org.scalajs.dom.{html, _}

import scalatags.JsDom
import scalatags.JsDom.all._
import scalatags.JsDom.tags2._

object TodoListComponent {
  import Context._

  // TODO: Udash should provide type alias CastableSeqProperty[A] = io.udash.properties.SeqProperty[A, _ <: CastableProperty[A]]
  type CastableSeqProperty[A] = io.udash.properties.SeqProperty[A, _ <: CastableProperty[A]]

  def apply(todoList: CastableSeqProperty[TodoElement],
            filter: ReadableProperty[TodoListFilter],
            toggleAllState: Property[Boolean],
            setAllItemsCompleted: () => Any,
            startItemEdit: (ModelProperty[TodoElement]) => Any,
            cancelItemEdit: (ModelProperty[TodoElement]) => Any,
            endItemEdit: (ModelProperty[TodoElement]) => Any,
            deleteItem: (ModelProperty[TodoElement]) => Any): JsDom.TypedTag[html.Element] =
    section(
      cls := "main",
      showIfListIsNotEmpty(todoList)
    )(
      Checkbox(
        toggleAllState,
        cls := "toggle-all",
        onclick :+= ((ev: Event) => { setAllItemsCompleted(); false })
      ),
      produce(filter)(f =>
        ul(cls := "todo-list")(
          repeat(todoList.filter(f.matcher))(
            (item: CastableProperty[TodoElement]) => {
              val todo = item.asModel
              TodoElementComponent(
                todo,
                () => startItemEdit(todo),
                () => cancelItemEdit(todo),
                () => endItemEdit(todo),
                () => deleteItem(todo)
              ).render
            }
          )
        ).render
      )
    )

  private def showIfListIsNotEmpty(list: ReadableSeqProperty[_]) =
    bindAttribute(list)((list: Seq[_], el: Element) => {
      if (list.isEmpty) jQ(el).hide()
      else jQ(el).show()
    })
}
