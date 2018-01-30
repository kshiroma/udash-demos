package io.udash.demos.files.rpc

import io.udash.rpc._

@RPC
trait MainClientRPC {
  def fileStorageUpdated(): Unit
}
