scalacOptions ++= Seq("-deprecation", "-Xlint", "-unchecked", "-language:_")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.1")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.4.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2")

// https://github.com/sbt/sbt/issues/2217
fullResolvers ~= {_.filterNot(_.name == "jcenter")}
