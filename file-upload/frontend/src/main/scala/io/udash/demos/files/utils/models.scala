package io.udash.demos.files.utils

case class JSFileData(name: String, size: Int)

sealed trait FileUploadState
object FileUploadState {
  case object InProgress extends FileUploadState
  case object Completed extends FileUploadState
  case object Failed extends FileUploadState
  case object Cancelled extends FileUploadState
}

trait FileUploadModel {
  def files: Seq[JSFileData]
  def state: FileUploadState
  def bytesSent: Int
  def bytesTotal: Int
}
