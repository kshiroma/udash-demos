package io.udash.demos.files.views

import io.udash._
import io.udash.demos.files.IndexState

object ErrorViewFactory extends StaticViewFactory[IndexState.type](() => new ErrorView)

class ErrorView extends FinalView {
  import scalatags.JsDom.all._

  private val content = h3(
    "URL not found!"
  ).render

  override def getTemplate: Modifier = content
}