package io.udash.demos.files.rpc

import scala.collection.mutable

class RPCService extends MainClientRPC {
  private val listeners = mutable.Set.empty[() => Unit]

  def listenStorageUpdate(callback: () => Unit) =
    listeners += callback

  override def fileStorageUpdated(): Unit =
    listeners.foreach(_.apply())
}

