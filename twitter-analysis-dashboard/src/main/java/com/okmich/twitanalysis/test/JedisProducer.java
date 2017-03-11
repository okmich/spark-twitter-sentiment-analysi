/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.twitanalysis.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

/**
 *
 * @author datadev
 */
public class JedisProducer {

    private final Jedis jedis;

    public static final String CHANNEL = "tweet_results";

    private JedisProducer() {
        this.jedis = new Jedis(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    }

    /**
     *
     * @param event
     */
    public void publish(String event) {
        jedis.publish(CHANNEL, event);
    }

    public static void main(String[] args) throws InterruptedException {
        JedisProducer producer = new JedisProducer();
        Random rand = new Random();
        Map<String, Integer> map = new HashMap<>(3);
        while (true) {
            Thread.sleep(rand.nextInt(3000));
            map.put("NEGATIVE", rand.nextInt(20));
            map.put("NEUTRAL", rand.nextInt(20) - 4);
            map.put("POSITIVE", rand.nextInt(20));
            producer.publish(createMessage(map));
        }
    }

    static String createMessage(Map<String, Integer> payload) {
        StringBuilder sb = new StringBuilder("");
        for (String key : payload.keySet()) {
            sb.append(key).append(":").append(payload.get(key)).append("|");
        }
        String msg = sb.toString().substring(0, sb.length() - 1);
        return msg;
    }

}
