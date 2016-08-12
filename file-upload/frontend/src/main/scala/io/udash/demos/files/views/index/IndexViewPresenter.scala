package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.IndexState

object IndexViewPresenter extends ViewPresenter[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = {
    import io.udash.demos.files.Context._
    val model = Property[String]
    val presenter = new IndexPresenter(model)
    val view = new IndexView(model, presenter)
    (view, presenter)
  }
}
