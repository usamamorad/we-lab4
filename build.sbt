name := "we15-lab4"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaCore,
  cache,
  javaWs,
  javaJpa,
  "org.apache.httpcomponents" % "httpclient" % "4.2.3",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final",
  "com.google.code.gson" % "gson" % "2.2",
  "org.twitter4j" % "twitter4j-core" % "4.0.3", //Twitter4j API
  "org.apache.jena" % "apache-jena-libs" % "2.13.0" exclude("org.apache.httpcomponents", "httpclient")
)
