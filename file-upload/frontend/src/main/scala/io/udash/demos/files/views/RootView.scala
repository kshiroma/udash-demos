package io.udash.demos.files.views

import io.udash._
import io.udash.bootstrap.utils.UdashJumbotron
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.css.CssView
import io.udash.demos.files.RootState
import io.udash.demos.files.views.components.Header

import scalatags.JsDom.tags2.main

object RootViewFactory extends StaticViewFactory[RootState.type](() => new RootView)

class RootView extends ContainerView with CssView {
  import scalatags.JsDom.all._

  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    Header.getTemplate,
    main(BootstrapStyles.container)(
      div(
        UdashJumbotron(
          h1("file-upload"),
          p("Welcome in the Udash file upload demo!")
        ).render,
        childViewContainer
      )
    )
  ).render

  override def getTemplate: Modifier = content
}