package io.udash.demos.rest.views.index

import io.udash._
import io.udash.demos.rest.IndexState

object IndexViewPresenter extends ViewPresenter[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = {
    import io.udash.demos.rest.Context._
    val model = ModelProperty[IndexViewModel]
    val presenter = new IndexPresenter(model)
    val view = new IndexView(model, presenter)
    (view, presenter)
  }
}
