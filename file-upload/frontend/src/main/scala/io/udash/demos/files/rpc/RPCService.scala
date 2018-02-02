package io.udash.demos.files.rpc

import io.udash.utils.{CallbacksHandler, Registration}

class RPCService extends MainClientRPC {
  private val listeners = new CallbacksHandler[Unit]

  def listenStorageUpdate(callback: () => Unit): Registration =
    listeners.register({ case () => callback() })

  override def fileStorageUpdated(): Unit =
    listeners.fire(())
}

