import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  val udashCoreVersion = "0.7.0"
  val udashJQueryVersion = "1.2.0"

  val frontendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-frontend" % udashCoreVersion,
    "io.udash" %%% "udash-css-frontend" % udashCoreVersion,
    "io.udash" %%% "udash-jquery" % udashJQueryVersion
  ))
}