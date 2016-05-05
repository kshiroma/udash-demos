name := "todomvc"

version in ThisBuild := "0.2.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.11.8"
organization in ThisBuild := "io.udash"
crossPaths in ThisBuild := false
scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:existentials",
  "-language:dynamics",
  "-Xfuture",
  "-Xfatal-warnings",
  "-Xlint:_,-missing-interpolator,-adapted-args"
)

val generatedDir = file("generated")

val todomvc = project.in(file(".")).enablePlugins(ScalaJSPlugin)
  .settings(
    emitSourceMaps := true,
    persistLauncher := true,

    libraryDependencies ++= frontendDeps.value,

    /* move these files out of target/. Also sets up same file for both fast and full optimization */
    crossTarget  in (Compile, fullOptJS)                     := generatedDir,
    crossTarget  in (Compile, fastOptJS)                     := generatedDir,
    crossTarget  in (Compile, packageJSDependencies)         := generatedDir,
    crossTarget  in (Compile, packageScalaJSLauncher)        := generatedDir,
    crossTarget  in (Compile, packageMinifiedJSDependencies) := generatedDir,
    artifactPath in (Compile, fastOptJS)                     :=
      ((crossTarget in (Compile, fastOptJS)).value / ((moduleName in fastOptJS).value + "-opt.js"))
  )