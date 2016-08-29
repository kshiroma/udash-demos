package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.IndexState

class IndexPresenter(model: ModelProperty[UploadViewModel]) extends Presenter[IndexState.type] {
  import io.udash.demos.files.Context._

  override def handleState(state: IndexState.type): Unit = ()

  private val uploader = new FileUploader(Url("upload/"))

  def uploadSelectedFiles(): Unit =
    uploader
      .upload("files", model.subSeq(_.selectedFiles).get)
      .listen(model.subProp(_.state).set)
}
