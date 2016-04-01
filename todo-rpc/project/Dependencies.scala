import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies extends Build {
  val udashCoreVersion = "0.1.0"
  val udashRPCVersion = "0.1.0"
  val udashI18NVersion = "0.1.0-SNAPSHOT"

  val logbackVersion = "1.1.3"
  val jettyVersion = "9.3.7.v20160115"

  val crossDeps = Def.setting(Seq[ModuleID](
    "io.udash" % "udash-core-shared" % udashCoreVersion,
    "io.udash" % "udash-rpc-shared" % udashRPCVersion,
    "io.udash" % "udash-i18n-shared" % udashI18NVersion
  ))

  val frontendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-frontend" % udashCoreVersion,
    "io.udash" %%% "udash-rpc-frontend" % udashRPCVersion,
    "io.udash" %%% "udash-i18n-frontend" % udashI18NVersion
  ))

  val frontendJSDeps = Def.setting(Seq[org.scalajs.sbtplugin.JSModuleID](
  ))

  val backendDeps = Def.setting(Seq[ModuleID](
    "io.udash" % "udash-rpc-backend" % udashRPCVersion,
    "io.udash" % "udash-i18n-backend" % udashI18NVersion,
    "org.eclipse.jetty" % "jetty-server" % jettyVersion,
    "org.eclipse.jetty" % "jetty-servlet" % jettyVersion,
    "org.eclipse.jetty.websocket" % "websocket-server" % jettyVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion
  ))
}