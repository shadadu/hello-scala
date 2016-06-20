name := """hello-scala"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "1.6.0"

//resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

resolvers += "Spark Packages Repo" at "https://raw.githubusercontent.com/ankurdave/maven-repo/master"

//libraryDependencies += "amplab" %% "spark-indexedrdd" % "0.3"

fork in run := true