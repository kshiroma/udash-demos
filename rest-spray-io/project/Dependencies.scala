import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies extends Build {
  val udashVersion = "0.3.0-rc.2"
  val udashJQueryVersion = "1.0.0"

  val logbackVersion = "1.1.3"
  val jettyVersion = "9.3.8.v20160314"
  val akkaVersion = "2.4.7"
  val sprayVersion = "1.3.1"

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
    "org.eclipse.jetty" % "jetty-server" % jettyVersion,
    "org.eclipse.jetty" % "jetty-servlet" % jettyVersion,
    "org.eclipse.jetty.websocket" % "websocket-server" % jettyVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-servlet" % sprayVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
  ))
}