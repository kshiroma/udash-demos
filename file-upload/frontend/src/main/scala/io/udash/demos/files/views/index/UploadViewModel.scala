package io.udash.demos.files.views.index

import org.scalajs.dom._

import io.udash.demos.files.UploadedFile
import io.udash.utils.FileUploader.FileUploadModel

trait UploadViewModel {
  def state: FileUploadModel
  def selectedFiles: Seq[File]
  def uploadedFiles: Seq[UploadedFile]
}
