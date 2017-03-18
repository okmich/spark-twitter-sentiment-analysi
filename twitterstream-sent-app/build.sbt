name := "Twitter Streaming and Sentiment Analysis App"
version := "1.0"
scalaVersion := "2.10.5"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.0" % "provided"
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.0" % "provided"
libraryDependencies += "redis.clients" % "jedis" % "2.9.0"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" classifier "models"