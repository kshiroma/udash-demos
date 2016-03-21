import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies extends Build {
  val udashCoreVersion = "0.1.0"
  val uPickleVersion = "0.3.8"

  val frontendDeps = Def.setting(Seq[ModuleID](
    "com.lihaoyi" %%% "upickle" % uPickleVersion,
    "io.udash" %%% "udash-core-frontend" % udashCoreVersion
  ))
}