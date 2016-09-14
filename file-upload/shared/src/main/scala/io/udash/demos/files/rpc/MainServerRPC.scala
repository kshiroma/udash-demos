package io.udash.demos.files.rpc

import io.udash.demos.files.UploadedFile
import io.udash.rpc._

import scala.concurrent.Future

@RPC
trait MainServerRPC {
  def loadUploadedFiles(): Future[Seq[UploadedFile]]
}
