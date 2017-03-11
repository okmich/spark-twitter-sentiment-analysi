package redis

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

object RedisProducer extends java.io.Serializable {
	//the channel to send events to
    private val  CHANNEL : String = "tweet_results"

    def publishResults(results : String)  = {
		//the redis endpoint object
		val jedis : Jedis = new Jedis("127.0.0.1", 6379)
		jedis.publish(CHANNEL, results)
		jedis.disconnect
    }
}