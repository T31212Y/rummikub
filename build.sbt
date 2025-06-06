val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Rummikub",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
      "org.scalatestplus" %% "mockito-5-12" % "3.2.19.0" % "test"
    ),

    testFrameworks += new TestFramework("org.scalatest.tools.Framework")
  )