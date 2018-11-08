package jp.co.camnet.dekitayo.view


import io.udash.StaticViewFactory
import io.udash.core.FinalView
import jp.co.camnet.dekitayo.DekitayoErrorState


object DekitayoErrorView extends StaticViewFactory[DekitayoErrorState.type](() => new DekitayoErrorView)

class DekitayoErrorView extends FinalView {

  import scalatags.JsDom.all._

  override val getTemplate: Modifier = {
    h3("URL not found!")
  }
}
