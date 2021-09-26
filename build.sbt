name := "frege"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("ninja.scala.frege")

lazy val models = project.in(file("frege-models"))

lazy val commons = project.in(file("frege-commons"))

lazy val core = project
  .in(file("frege-core"))
  .dependsOn(models, commons)
  .aggregate(models, commons)
  .settings(
    libraryDependencies ++= Seq(
      "it.unimi.dsi" % "fastutil" % "8.5.6"
    )
  )

lazy val client =
  project.in(file("frege-client")).dependsOn(core).aggregate(core)

lazy val benchmarks =
  project
    .in(file("frege-benchmarks"))
    .dependsOn(core)
    .aggregate(core)
    .enablePlugins(JmhPlugin)
