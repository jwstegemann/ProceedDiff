enablePlugins(ScalaJSPlugin)

name := "ProceedDiff"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.lihaoyi"   %%  "utest"         % "0.4.3" % "test",
  "org.scala-js"  %%%  "scalajs-dom"    % "0.9.0"
)

testFrameworks += new TestFramework("utest.runner.Framework")

scalaJSStage in Global := FastOptStage
