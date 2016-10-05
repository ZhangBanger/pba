name := """messaging-cluster"""

version := "0.1"

scalaVersion := "2.11.8"

lazy val akkaVersion = "2.4.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.12",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.42",
  "io.grpc" % "grpc-all" % "1.0.1",
//  "io.netty" % "netty-tcnative-boringssl-static" % "1.1.33.Fork22",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test")

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)
