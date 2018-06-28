package io.udash.demos.files.rpc

import io.udash.rpc._

trait MainClientRPC {
  def fileStorageUpdated(): Unit
}
object MainClientRPC extends DefaultClientUdashRPCFramework.RPCCompanion[MainClientRPC]