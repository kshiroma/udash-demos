package io.udash.demos.files

import io.udash.demos.files.jetty.ApplicationServer

object Launcher {
  def main(args: Array[String]): Unit = {
    val server = new ApplicationServer(8080, "backend/target/UdashStatic/WebContent")
    server.start()
  }
}

       