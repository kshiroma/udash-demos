package io.udash.demos.rest.views

import io.udash._
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.bootstrap.utils.{UdashJumbotron, UdashPageHeader}
import io.udash.demos.rest.RootState
import org.scalajs.dom.Element

import scalatags.JsDom.tags2.main
import io.udash.demos.rest.views.components.Header

object RootViewPresenter extends DefaultViewPresenterFactory[RootState.type](() => new RootView)

class RootView extends View {
  import io.udash.demos.rest.Context._
  import scalatags.JsDom.all._

  private val child: Element = div().render

  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    Header.getTemplate,
    main(BootstrapStyles.container)(
      div(
        UdashJumbotron(
          h1("rest-spray-io"),
          p("Welcome in the Udash REST and the Udash Bootstrap modules demo!")
        ).render,
        child
      )
    )
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {
    import io.udash.wrappers.jquery._
    view.getTemplate.applyTo(child)
  }
}