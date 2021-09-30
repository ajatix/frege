name := "frege"

version in Global := "0.1"

scalaVersion in Global := "2.12.15"

organization in Global := "ninja.scala"

lazy val models = project
  .in(file("frege-models"))
  .settings(
    name := "frege-models"
  )

lazy val testkit =
  project
    .in(file("frege-testkit"))
    .dependsOn(models)
    .aggregate(models)
    .settings(
      name := "frege-testkit",
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.2.10",
        "org.scalacheck" %% "scalacheck" % "1.15.4"
      )
    )

lazy val commons = project
  .in(file("frege-commons"))
  .settings(
    name := "frege-commons"
  )

lazy val core = project
  .in(file("frege-core"))
  .dependsOn(models, commons, testkit % "compile->test")
  .aggregate(models, commons)
  .settings(
    name := "frege-core",
    libraryDependencies ++= Seq(
      "it.unimi.dsi" % "fastutil" % "8.5.6",
      "org.typelevel" %% "cats-core" % "2.6.1"
    )
  )

lazy val client =
  project
    .in(file("frege-client"))
    .dependsOn(core)
    .aggregate(core)
    .settings(
      name := "frege-client"
    )

lazy val benchmarks =
  project
    .in(file("frege-benchmarks"))
    .dependsOn(core, testkit)
    .aggregate(core, testkit)
    .enablePlugins(JmhPlugin)
    .settings(
      name := "frege-benchmarks"
    )
