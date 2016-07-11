import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies extends Build {
  val udashVersion = "0.3.0-rc.2"
  val udashJQueryVersion = "1.0.0"
  val uPickleVersion = "0.4.0"

  val logbackVersion = "1.1.3"
  val jettyVersion = "9.3.7.v20160115"

  val crossDeps = Def.setting(Seq[ModuleID](
    "io.udash" %% "udash-core-shared" % udashVersion,
    "io.udash" %% "udash-rpc-shared" % udashVersion
  ))

  val frontendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-frontend" % udashVersion,
    "io.udash" %%% "udash-rpc-frontend" % udashVersion,
    "io.udash" %%% "udash-jquery" % udashJQueryVersion,
    "com.lihaoyi" %%% "upickle" % uPickleVersion
  ))

  val backendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %% "udash-rpc-backend" % udashVersion,
    "org.eclipse.jetty" % "jetty-server" % jettyVersion,
    "org.eclipse.jetty" % "jetty-servlet" % jettyVersion,
    "org.eclipse.jetty.websocket" % "websocket-server" % jettyVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion
  ))
}