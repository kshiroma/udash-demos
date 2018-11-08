package jp.co.camnet.dekitayo.view.dekitayo

import io.udash._
import jp.co.camnet.dekitayo.DekitayoState

object DekitayoViewFactory extends ViewFactory[DekitayoState] {
  override def create(): (View, Presenter[DekitayoState]) = {
    val model = ModelProperty(DekitayoViewModel(Seq.empty, DekitayoFilter.All, "", true))
    val presenter: DekitayoPresenter = new DekitayoPresenter(model)
    val view = new DekitayoView(model, presenter)
    (view, presenter)
  }
}
