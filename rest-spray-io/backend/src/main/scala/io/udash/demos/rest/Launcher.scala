package io.udash.demos.rest

import io.udash.demos.rest.jetty.ApplicationServer

object Launcher {
  def main(args: Array[String]): Unit = {
    val server = new ApplicationServer(8080, "backend/target/UdashStatic/WebContent")
    server.start()
  }
}

       