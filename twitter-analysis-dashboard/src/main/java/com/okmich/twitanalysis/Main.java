/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.twitanalysis;

import com.okmich.twitanalysis.gui.ApplicationFrame;
import com.okmich.twitanalysis.redis.JedisSubscriber;

/**
 *
 * @author Michael.Enudi
 */
public class Main {

    private final ApplicationFrame gui;
    private final ActionObserver messageClient;

    /**
     * Creates new form FirstChart
     */
    public Main() {
        this.gui = new ApplicationFrame();
        this.messageClient = JedisSubscriber.getInstance();
        this.messageClient.setActionExecutor(gui);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final Main app = new Main();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.gui.setVisible(true);
            }
        });
        app.startObserver();
    }

    private void startObserver() {
        this.messageClient.connect();
    }

}
