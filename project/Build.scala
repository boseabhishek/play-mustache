import play.sbt.PlayImport._
import play.sbt.PlayScala
import sbt.Keys._
import sbt._

object Build extends Build {
  val moduleName = "play-mustache"

  lazy val build = (project in file(".")).enablePlugins(PlayScala)
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(javaOptions in Test += "-Dconfig.resource=application.test.conf")
    .settings(
    name := moduleName,
    organization := "kissthinker",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:reflectiveCalls",
      "-language:postfixOps",
      "-Yrangepos",
      "-Yrepl-sync"),
    ivyScala := ivyScala.value map {
      _.copy(overrideScalaVersion = true)
    },
    resolvers ++= Seq(
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
      "Kamon Repository" at "http://repo.kamon.io"
    )
  )
  .settings(libraryDependencies ++= {
    Seq(
      jdbc,
      cache,
      ws,
      specs2 % Test,
      "org.webjars" %% "webjars-play" % "2.4.0-1",
      "org.webjars" % "bootstrap" % "4.0.0-alpha.2",
      "org.webjars" % "jquery" % "3.0.0-alpha1",
      "org.webjars" % "mustachejs" % "2.2.1"
    ) ++ Seq(
      specs2 % Test
    )
  })

  val root = sys.props.get("local-build") match {
    case Some("true") =>
      println("*** Local Build - Using relative and not correctly versioned modules ***")
      build.dependsOn(ProjectRef(file("../play-mustache-lib"), "play-mustache-lib") % "test->test;compile->compile")

    case _ =>
      /*
      NORMALLY YOU WOULD USE THE FOLLOWING, BUT WE SHALL DECLARE THE DEPENDENCY DIRECTLY TO GITHUB

      build.settings(libraryDependencies ++= {
        val `play-mustache-verson` = "1.0.0-SNAPSHOT"

        Seq(
          "kissthinker" %% "play-mustache-lib" % `play-mustache-verson` withSources()
        ) ++ Seq(
          "kissthinker" %% "play-mustache-lib" % `play-mustache-verson` % Test classifier "tests" withSources()
        )
      })
      */

      build.dependsOn(RootProject(uri("https://github.com/davidainslie/play-mustache-lib.git")))
  }
}