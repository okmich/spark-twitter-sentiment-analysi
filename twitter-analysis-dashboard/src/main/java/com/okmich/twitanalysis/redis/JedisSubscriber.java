/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.twitanalysis.redis;

import com.okmich.twitanalysis.ActionExecutor;
import com.okmich.twitanalysis.ActionObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Protocol;

/**
 *
 * @author m.enudi
 */
/**
 *
 * @author datadev
 */
public class JedisSubscriber extends JedisPubSub implements Runnable, ActionObserver {

    private final static JedisSubscriber ME = new JedisSubscriber();
    private final Jedis jedis;

    private Thread thread;
    private ActionExecutor actionExecutor;

    private static final Logger LOG = Logger.getLogger(JedisSubscriber.class.getName());

    private JedisSubscriber() {
        this.jedis = new Jedis(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    }

    /**
     *
     * @return
     */
    public static JedisSubscriber getInstance() {
        return ME;
    }

    @Override
    public void onMessage(String channel, String message) {
        LOG.log(Level.INFO, "message received {0}", message);
        try {
            this.actionExecutor.executeAction(processMessage(message));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        LOG.log(Level.INFO, "{0} has been subscribed to", channel);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        LOG.log(Level.INFO, "{0} has been unsubscribed from", channel);
    }

    @Override
    public void connect() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void disconnect() {
        this.unsubscribe();
        thread.interrupt();
    }

    @Override
    public void run() {
        this.jedis.subscribe(this, "tweet_results");
    }

    @Override
    public void setActionExecutor(ActionExecutor ae) {
        this.actionExecutor = ae;
    }

    private Map<String, Integer> processMessage(String message) {
        //proposed format is SENTIMENT_TAG:val|...
        Map<String, Integer> map = new HashMap<>();
        String[] res = message.split("\\|"), parts;
        for (String s : res) {
            parts = s.split(":");
            map.put(parts[0], Integer.valueOf(parts[1]));
        }
        return map;
    }

//    public static void main(String[] args) {
//        String testCase = "NEGATIVE:5|POSITIVE:10|NEUTRAL:2";
//        System.out.println(new JedisSubscriber().processMessage(testCase));
//    }
}
