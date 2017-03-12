
import java.io.Serializable

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.api.java._
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.twitter.TwitterUtils

import twitter4j.Status
import twitter4j.conf._
import twitter4j.auth.OAuthAuthorization

import com.github.vspiewak.util.SentimentAnalysisUtils._

object Main extends Serializable {

	def main(args: Array[String]) : Unit = {
		if (args.length < 1){
			println("Please specify the search string as argment")
			System.exit(-1)
		}
		val searchString = args mkString " "
		val sparkConf = new SparkConf().setAppName("Twitter Streaming")
		val sc = new StreamingContext(sparkConf, Seconds(5))

		System.setProperty("twitter4j.oauth.consumerKey", "GRn40epjhYSrfmlSJH4rNemG8")
		System.setProperty("twitter4j.oauth.consumerSecret", "Dx8GxpCkkUvp9FoYcXNNkNNYiRmHQjSDgfxoTs4m4JBVDxr4q1")
		System.setProperty("twitter4j.oauth.accessToken", "1421823854-zyPjn7cHpMfbxJm2Pg8jzKKvouFdh7kqhHp0Dcw")
		System.setProperty("twitter4j.oauth.accessTokenSecret", "69XjGprH4mqg5DW7LfomX3cV1R0TjbeNxx4vDnw2N0ENN")


		val inputDStream : ReceiverInputDStream[Status] = TwitterUtils.createStream(sc, None, Array(searchString))

		inputDStream.foreachRDD(processTweetRDD(_))

		sc.start
		sc.awaitTermination
	}

	def processTweetRDD(rdd: RDD[twitter4j.Status]) : Unit ={
		//do cl
		val results : scala.collection.Map[String, Long] = rdd.filter(_.getLang == "en").map(_.getText).map((txt: String) => {
			detectSentiment(txt).toString
		}).countByValue
		//reduce resultRDD on count by classification

		// //sample data
		// import scala.util.Random
		// val rand = new Random
		// val results = Array(("POSITIVE", rand.nextInt(30)), 
		// 	("NEGATIVE",rand.nextInt(30)), 
		// 	("NEUTRAL", rand.nextInt(20))).map(a => {
		// 		a._1 + ":" + a._2.toString}).mkString("|")

		val message = results.toArray.map((a:(String, Long)) => {
		 		a._1 + ":" + a._2.toString}).mkString("|")
		//send to message broker
		redis.RedisProducer.publishResults(message)
	}
}