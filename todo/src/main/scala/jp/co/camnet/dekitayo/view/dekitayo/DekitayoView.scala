package jp.co.camnet.dekitayo.view.dekitayo

import io.udash._
import io.udash.css._
import io.udash.properties.single.ReadableProperty
import io.udash.todo.ApplicationContext
import jp.co.camnet.dekitayo.DekitayoState
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{Event, KeyboardEvent}

import scala.concurrent.duration.DurationLong
import scala.language.postfixOps

class DekitayoView(model: ModelProperty[DekitayoViewModel], presenter: DekitayoPresenter) extends FinalView with CssView {

  import scalatags.JsDom.all._
  import scalatags.JsDom.tags2.section

  private val isTodoListNonEmpty: ReadableProperty[Boolean] = model.subSeq(_.dekitayos).transform(_.nonEmpty)


  private val isCompleteDekitayoListNonEmpty: ReadableProperty[Boolean] =
    model.subSeq(_.dekitayos).filter(DekitayoFilter.Completed.matcher).transform(_.nonEmpty)


  private def headerTemplate = {

    header(cls := "header")(
      TextInput(model.subProp(_.newDekitayoName), debounce = 0 millis)(
        cls := "new-todo",
        placeholder := "What needs to be done",
        autofocus := true,
        onkeydown :+= ((ev: KeyboardEvent) => {
          if (ev.keyCode == KeyCode.Enter) {
            presenter.addDekitayo()
            true
          } else {
            false
          }
        })
      ),

      Select(model.subProp(_.newSiteName))(
        cls:="newTodo",
        placeholder:= ""
      )
    )
  }


  private val listTemplate = {
    section(cls := "main")(
      Checkbox(model.subProp(_.toggleAllChecked))(
        cls := "toggle-all",
        onclick :+= ((_: Event) => {
          presenter.setItemsCompleted()
          false
        })
      ),
      produce(model.subProp(_.dekitayoFilter))(filter =>
        ul(cls := "todo-list")(
          repeat(model.subSeq(_.dekitayos).filter(filter.matcher))(
            (item: CastableProperty[Dekitayo]) =>
              listItemTemplate(item.asModel).render
          )
        ).render
      )
    )
  }

  private val footerTemplate = {
    footer(cls := "footer")(
      produce(model.subSeq(_.dekitayos).filter(DekitayoFilter.Active.matcher))(dekitayos => {
        val size: Int = dekitayos.size
        val pluralization = if (size == 1) "item" else "items"
        span(cls := "todo-count")(s"$size $pluralization left").render
      }),
      ul(cls := "filters")(
        for ((name, filter) <- Seq(("All", DekitayoFilter.All), ("Active", DekitayoFilter.Active), ("Completed", DekitayoFilter.Completed)))
          yield li(footerButtonTemplate(name, DekitayoState(filter).url(ApplicationContext.dekitayoApplicatonInstance), filter))
      ),
      showIf(isCompleteDekitayoListNonEmpty)(Seq(
        button(
          cls := "clear-completed",
          onclick :+= ((_: Event) => presenter.clearCompleted(), true)
        )("Clear complete").render
      ))
    )
  }

  override val getTemplate: Modifier = div(
    headerTemplate,
    showIf(isTodoListNonEmpty)(Seq(
      listTemplate.render,
      footerTemplate.render
    ))
  )

  private def listItemTemplate(item: ModelProperty[Dekitayo]) = {
    val editName = Property("")
    val editorInput = TextInput(editName, debounce = 0 millis)(
      cls := "edit",
      onkeydown :+= ((ev: KeyboardEvent) => {
        if (ev.keyCode == KeyCode.Enter) {
          presenter.endItemEdit(item, editName)
          true
        } else if (ev.keyCode == KeyCode.Escape) {
          presenter.cancelItemEdit(item)
          true
        } else {
          false
        }
      }),
      onblur :+= ((ev: Event) => {
        presenter.endItemEdit(item, editName)
        true
      })
    ).render

    val stdView = div(cls := "view")(
      Checkbox(item.subProp(_.completed))(cls := "toggle"),
      label(
        ondblclick :+= ((ev: Event) => {
          presenter.startItemEdit(item, editName)
          editorInput.focus()
        }, true)
      )(bind(item.subProp(_.name))),
      button(
        cls := "destroy",
        onclick :+= ((ev: Event) => presenter.deleteItem(item.get), true)
      )
    )

    li(
      CssStyleName("completed").styleIf(item.subProp(_.completed)),
      CssStyleName("editing").styleIf(item.subProp(_.editing))
    )(stdView, editorInput)

  }

  private def footerButtonTemplate(title: String, link: String, expectedFilter: DekitayoFilter) = {
    val isSelected = model.subProp(_.dekitayoFilter).transform(_ == expectedFilter)
    a(href := link, CssStyleName("selected").styleIf(isSelected))(title)
  }

}
