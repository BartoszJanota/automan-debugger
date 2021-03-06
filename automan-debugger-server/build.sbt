
name := "automan-debugger-server"

version := "1.0-SNAPSHOT"

exportJars := true

organization := "edu.umass.cs.plasma"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0-RC4",
  "edu.umass.cs" %% "automan" % "1.1.8-SNAPSHOT",
  "io.spray" %% "spray-json" % "1.3.1",
  "io.spray" %% "spray-client" % "1.3.3",
  "io.spray" %% "spray-routing" % "1.3.2",
  "io.backchat.hookup" % "hookup_2.11" % "0.3.0"
)
