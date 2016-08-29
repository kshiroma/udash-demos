package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.{IndexState, UploadedFile}
import io.udash.utils.FileUploader.FileUploadModel
import org.scalajs.dom._

trait UploadViewModel {
  def state: FileUploadModel
  def selectedFiles: Seq[File]

  def uploadedFiles: Seq[UploadedFile]
}

object IndexViewPresenter extends ViewPresenter[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = {
    import io.udash.demos.files.Context._
    val model = ModelProperty[UploadViewModel]
    val presenter = new IndexPresenter(model)
    val view = new IndexView(model, presenter)
    (view, presenter)
  }
}
