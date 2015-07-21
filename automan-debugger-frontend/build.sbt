// Turn this project into a Scala.js project by importing these settings
enablePlugins(ScalaJSPlugin)

name := "automan-debugger-frontend"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

persistLauncher in Compile := true

persistLauncher in Test := false

testFrameworks += new TestFramework("utest.runner.Framework")

unmanagedBase := baseDirectory.value / "lib"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
  "edu.umass.cs" %% "automan" % "0.5-SNAPSHOT",
  "io.spray" %% "spray-json" % "1.3.1",
  "io.spray" %% "spray-client" % "1.3.3",
  "io.spray" %% "spray-routing" % "1.3.2"
)