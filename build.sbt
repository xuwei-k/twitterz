scalaVersion := "2.11.1"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M1" cross CrossVersion.full)

crossScalaVersions := scalaVersion.value :: "2.10.4" :: Nil

name := "twitterz"

organization := "com.github.xuwei-k"

startYear := Some(2014)

description := "Purely functional twitter api clinet"

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  Nil
)

scalacOptions ++= (
  if(scalaVersion.value.startsWith("2.10"))
    Nil
  else
    Seq("-Ywarn-unused", "-Ywarn-unused-import")
)

libraryDependencies ++= (
  ("org.scalaz" %% "scalaz-core" % "7.1.0-M7") ::
  ("org.twitter4j" % "twitter4j-core" % "4.0.1") ::
  Nil
)

pomExtra := (
<url>https://github.com/xuwei-k/twitterz</url>
<developers>
  <developer>
    <id>xuwei-k</id>
    <name>Kenji Yoshida</name>
    <url>https://github.com/xuwei-k</url>
  </developer>
</developers>
<scm>
  <url>git@github.com:xuwei-k/twitterz.git</url>
  <connection>scm:git:git@github.com:xuwei-k/twitterz.git</connection>
  <tag>{if(isSnapshot.value) gitHash else { "v" + version.value }}</tag>
</scm>
)

def gitHash: String = scala.util.Try(
  sys.process.Process("git rev-parse HEAD").lines_!.head
).getOrElse("master")

scalacOptions in (Compile, doc) ++= {
  val tag = if(isSnapshot.value) gitHash else { "v" + version.value }
  Seq(
    "-sourcepath", baseDirectory.value.getAbsolutePath,
    "-doc-source-url", s"https://github.com/xuwei-k/twitterz/tree/${tag}€{FILE_PATH}.scala"
  )
}

lazy val twitterz = project.in(file(".")).dependsOn(
  ProjectRef(uri("git://github.com/xuwei-k/macro-annotation-sample.git#47896fc1c086bf57be9fc"), "macros")
)
