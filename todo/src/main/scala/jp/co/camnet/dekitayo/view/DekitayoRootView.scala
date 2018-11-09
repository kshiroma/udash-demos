package jp.co.camnet.dekitayo.view

import io.udash._
import jp.co.camnet.dekitayo._

object DekitayoRootViewFactory extends StaticViewFactory[DekitayoRootState.type](() => new DekitayoRootView)


class DekitayoRootView extends ContainerView {

  import scalatags.JsDom.all._

  override val getTemplate: Modifier = {
    div(
      h1("dekita-yo"),
      childViewContainer
    )
  }
}