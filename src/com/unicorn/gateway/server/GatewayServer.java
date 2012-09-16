/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.server;

import com.unicorn.gateway.process.*;
import com.unicorn.gateway.queue.MessageQueue;
import com.unicorn.gateway.queue.ResponseQueue;
import java.net.ServerSocket;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class GatewayServer {

    private String smsAddress;
    private int listenPort, smsPort;
    Logger logger = Logger.getLogger(GatewayServer.class);

    public GatewayServer(String smsAddress, int smsPort, int listenPort) {
        this.smsAddress = smsAddress;
        this.smsPort = smsPort;
        this.listenPort = listenPort;
    }

    public boolean initialise() throws Exception {

        //read sms properties
        logger.info("Reading properties file .... ");
        LoadProperties lp = new LoadProperties();
        GatewayProperties properties = lp.loadProperties(lp.getPropertiesFilePath());
        logger.info("Properties file loaded from .... " + lp.getPropertiesFilePath());

        //start the binding process
        logger.info("Starting binding thread ..... ");
        
        BindThread bind = new BindThread(properties);
        new Thread(bind).start();

        //start the listening port
        logger.info("Gateway initialising .... ");

        ServerSocket serverSocket = new ServerSocket(listenPort);
        logger.info("Listening for request ..... " + serverSocket.getLocalPort());
        logger.info("Server started ....");

        //initialise all
        logger.info("Initialising Message Queue ... ");
        MessageQueue queue = new MessageQueue();
        
        logger.info("Initialising request spooler .... ");
        RequestSpooler spooler = new RequestSpooler(properties);
        spooler.spool();
        
        logger.info("Initialising response queue .... ");
        ResponseQueue rq = new ResponseQueue();
        
        logger.info("Starting message listener .... ");
        ServerProcess sp = new ServerProcess(serverSocket,queue,rq);
        new Thread(sp).start();

        return false;
    }

    public String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getSmsPort() {
        return smsPort;
    }

    public void setSmsPort(int smsPort) {
        this.smsPort = smsPort;
    }

    public static void main(String[] args) {
        GatewayServer gs = new GatewayServer("192.168.1.120", 7039, 2000);
        try {
            gs.initialise();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GatewayServer.class).error(ex.getMessage());
        }
    }
}
