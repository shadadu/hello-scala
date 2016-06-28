name := """hello-scala"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "org.apache.spark" %% "spark-graphx" % "1.6.0"
libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.8.2.2"
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "1.5.2"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.6.0"

resolvers += "Spark Packages Repo" at "https://raw.githubusercontent.com/ankurdave/maven-repo/master"

fork in run := true