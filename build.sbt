name := """voicebanksLog"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "mysql" % "mysql-connector-java" % "5.1.34",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "font-awesome" % "4.2.0",
  "org.webjars" % "chartjs" % "26962ce",
  "com.typesafe.play" %% "play-json" % "2.3.7"
)
