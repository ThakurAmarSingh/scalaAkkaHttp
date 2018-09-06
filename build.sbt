name := "scalaAkkaHttp"

version := "0.1"

scalaVersion := "2.12.6"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.4",
  "com.typesafe.akka" %% "akka-stream" % "2.5.14",
    "com.typesafe.akka" %% "akka-actor" % "2.5.16",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % Test,
    "com.typesafe" % "config" % "1.3.2",
    "org.scalaj" %% "scalaj-http" % "2.4.1")
}


