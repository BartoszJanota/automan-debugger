// Turn this project into a Scala.js project by importing these settings
enablePlugins(ScalaJSPlugin)

name := "automan-debugger-frontend"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

persistLauncher in Compile := true

persistLauncher in Test := false

testFrameworks += new TestFramework("utest.runner.Framework")

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
  "edu.umass.cs" %% "automan" % "0.5-SNAPSHOT",
  "com.lihaoyi" %%% "upickle" % "0.2.8",
  "eu.unicredit" %%% "paths-scala-js" % "0.3.2",
  "com.github.japgolly.scalajs-react" %%% "core" % "0.9.0"
)

jsDependencies += "org.webjars" % "paths-js" % "0.3.2" / "paths.js"

jsDependencies +=
  "org.webjars" % "react" % "0.12.2" / "react-with-addons.js" commonJSName "React"