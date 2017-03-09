/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.twitanalysis.redis;

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
public class JedisSubscriber extends JedisPubSub implements Runnable {

    private final static JedisSubscriber ME = new JedisSubscriber();
    private final Jedis jedis;

    private Thread thread;

    private static final Logger LOG = Logger.getLogger(JedisSubscriber.class.getName());

    public JedisSubscriber() {
        this.jedis = new Jedis(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    }

    @Override
    public void onMessage(String channel, String message) {
        //ME.flowMediator.update(message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        LOG.log(Level.INFO, "{0} has been subscribed to", channel);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        LOG.log(Level.INFO, "{0} has been unsubscribed from", channel);
    }

//
//    @Override
//    public void connect() {
//        thread = new Thread(this);
//        thread.start();
//    }
//
//    @Override
//    public void disconnect() {
//        this.unsubscribe();
//        thread.interrupt();
//    }
    @Override
    public void run() {
        this.jedis.subscribe(this, "taxis");
    }
}
