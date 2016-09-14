package io.udash.demos.files.rpc

import io.udash.demos.files.UploadedFile
import io.udash.rpc._

@RPC
trait MainClientRPC {
  def fileStorageUpdated(): Unit
}
