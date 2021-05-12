ThisBuild / scalaVersion := "2.13.5"
ThisBuild / crossScalaVersions := Seq(scalaVersion.value)
ThisBuild / githubWorkflowPublishTargetBranches := Nil

enablePlugins(JmhPlugin)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-jawn" % "0.13.0",
  "org.scalameta" %% "munit" % "0.7.25" % Test
)
