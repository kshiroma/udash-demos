package io.udash.demos.files.views

import io.udash._
import io.udash.bootstrap.utils.UdashJumbotron
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.demos.files.RootState
import io.udash.demos.files.views.components.Header
import org.scalajs.dom.Element

import scalatags.JsDom.tags2.main

object RootViewPresenter extends DefaultViewPresenterFactory[RootState.type](() => new RootView)

class RootView extends View {
  import scalatags.JsDom.all._

  private val child: Element = div().render

  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    Header.getTemplate,
    main(BootstrapStyles.container)(
      div(
        UdashJumbotron(
          h1("file-upload"),
          p("Welcome in the Udash file upload demo!")
        ).render,
        child
      )
    )
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {
    view.getTemplate.applyTo(child)
  }
}