enablePlugins(ScalaJSPlugin)

name := "ProceedDiff"

version := "1.0"

scalaVersion := "2.11.8"

scalaJSStage in Global := FastOptStage

libraryDependencies ++= Seq(
  "com.lihaoyi"   %%  "utest"         % "0.4.3" % "test",
  "org.scala-js"  %%%  "scalajs-dom"    % "0.9.0",
  "org.scala-lang" % "scala-reflect" % "2.11.8"
)

testFrameworks += new TestFramework("utest.runner.Framework")

workbenchSettings

bootSnippet := "proceed.ProceedApp().main();"

refreshBrowsers <<= refreshBrowsers.triggeredBy(fastOptJS in Compile)
