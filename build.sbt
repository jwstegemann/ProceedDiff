enablePlugins(ScalaJSPlugin)

name := "proceed"
organization := "org.proceed"

version := "1.0"

scalaVersion in ThisBuild := "2.11.8"

run <<= run in Compile in core

scalaJSStage in core := FastOptStage
jsDependencies += RuntimeDOM

lazy val root = (project in file(".")).aggregate(macros, core)

lazy val macros = (project in file("macros")).settings(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.softwaremill.quicklens" %%% "quicklens" % "1.4.6"
  )
)

lazy val core = (project in file("core")).settings(
  libraryDependencies ++= Seq(
    "com.lihaoyi"   %%  "utest"         % "0.4.3" % "test",
    "org.scala-js"  %%%  "scalajs-dom"    % "0.9.0",
    "org.scala-lang" % "scala-reflect" % "2.11.8",
    "com.softwaremill.quicklens" %%% "quicklens" % "1.4.6",
    "com.github.japgolly.scalacss" %% "core" % "0.4.1"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
).enablePlugins(ScalaJSPlugin).dependsOn(macros)