package io.udash.todo.views

import io.udash._
import io.udash.todo.ErrorState
import org.scalajs.dom.Element

object ErrorViewPresenter extends DefaultViewPresenterFactory[ErrorState.type](() => new ErrorView)

class ErrorView extends View {
  import scalatags.JsDom.all._

  private val content = h3(
    "URL not found!"
  ).render

  override def getTemplate: Element = content

  override def renderChild(view: View): Unit = {}
}
