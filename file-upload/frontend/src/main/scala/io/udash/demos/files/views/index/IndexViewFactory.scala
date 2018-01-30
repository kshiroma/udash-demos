package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.IndexState

object IndexViewFactory extends ViewFactory[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = {
    val model = ModelProperty(new UploadViewModel())
    val presenter = new IndexPresenter(model)
    val view = new IndexView(model, presenter)
    (view, presenter)
  }
}
