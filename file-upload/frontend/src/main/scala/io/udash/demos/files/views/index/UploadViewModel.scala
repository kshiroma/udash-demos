package io.udash.demos.files.views.index

import org.scalajs.dom._
import io.udash.demos.files.UploadedFile
import io.udash.properties.HasModelPropertyCreator
import io.udash.utils.FileUploader.{FileUploadModel, FileUploadState}

class UploadViewModel(
  val state: FileUploadModel = new FileUploadModel(Seq.empty, FileUploadState.Completed, 0, 0),
  val selectedFiles: Seq[File] = Seq.empty,
  val uploadedFiles: Seq[UploadedFile] = Seq.empty
)

object UploadViewModel extends HasModelPropertyCreator[UploadViewModel]
