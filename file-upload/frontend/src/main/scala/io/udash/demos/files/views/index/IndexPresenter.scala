package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.IndexState

class IndexPresenter(model: Property[String]) extends Presenter[IndexState.type] {

  override def handleState(state: IndexState.type): Unit = ()
}
