name := "Fizzbuzz"

version := "0.1"

scalaVersion := "2.11.12"

organization := "com.examples"

parallelExecution in Test := false

test in assembly := {}

mainClass in (assembly) := Some("com.examples.FizzbuzzMain")
assemblyJarName in assembly := "Fizzbuzz.jar"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test