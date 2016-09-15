import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies extends Build {
  val udashCoreVersion = "0.4.0"
  val udashJQueryVersion = "1.0.0"
  val uPickleVersion = "0.4.0"

  val frontendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-frontend" % udashCoreVersion,
    "io.udash" %%% "udash-jquery" % udashJQueryVersion,
    "com.lihaoyi" %%% "upickle" % uPickleVersion
  ))
}