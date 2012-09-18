/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.server;

import com.unicorn.gateway.process.*;
import com.unicorn.gateway.queue.MessageQueue;
import com.unicorn.gateway.queue.ResponseQueue;
import java.net.ServerSocket;
import java.util.Properties;
import main.PropertyFileReader;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class GatewayServer {

    private int listenPort;
    private Logger logger = Logger.getLogger(GatewayServer.class);
    private LoadProperties lp = new LoadProperties();

    public GatewayServer(int listenPort) {
        this.listenPort = listenPort;
    }

    public boolean initialise() throws Exception {

        //read sms properties
        logger.info("Reading properties file .... ");
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
        ServerProcess sp = new ServerProcess(serverSocket, queue, rq, properties);
        new Thread(sp).start();

        return false;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public static void main(String[] args) {
        try {
            LoadProperties lp = new LoadProperties();
            Properties p = new PropertyFileReader(lp.getGatewayPath()).getAllProperties();
            Logger.getLogger(GatewayServer.class).info("Loaded default gateway properties .... ");
            GatewayServer gs = new GatewayServer(Integer.parseInt(p.getProperty("gateway-port")));
            Logger.getLogger(GatewayServer.class).info("Initialising server .... ");
            gs.initialise();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GatewayServer.class).error(ex.getMessage());
        }
    }
}
