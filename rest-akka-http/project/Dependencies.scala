import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  val udashVersion = "0.7.1"
  val udashJQueryVersion = "1.2.0"

  val logbackVersion = "1.1.3"
  val jettyVersion = "9.4.11.v20180605"
  val akkaVersion = "2.5.13"
  val akkaHttpVersion = "10.1.3"

  val crossDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-shared" % udashVersion,
    "io.udash" %%% "udash-rest-shared" % udashVersion
  ))

  val frontendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-frontend" % udashVersion,
    "io.udash" %%% "udash-bootstrap" % udashVersion,
    "io.udash" %%% "udash-jquery" % udashJQueryVersion
  ))

  val frontendJSDeps = Def.setting(Seq[org.scalajs.sbtplugin.JSModuleID](
  ))

  val backendDeps = Def.setting(Seq[ModuleID](
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
  ))
}
