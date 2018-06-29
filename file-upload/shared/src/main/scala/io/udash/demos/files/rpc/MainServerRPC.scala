package io.udash.demos.files.rpc

import io.udash.demos.files.UploadedFile
import io.udash.rpc._

import scala.concurrent.Future

trait MainServerRPC {
  def loadUploadedFiles(): Future[Seq[UploadedFile]]
}
object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]
