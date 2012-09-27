/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.server;

import com.swifta.gateway.process.*;
import com.swifta.gateway.queue.MessageQueue;
import com.swifta.gateway.queue.ResponseQueue;
import com.swifta.smpp.pool.SessionPool;
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

//        BindThread bind = new BindThread(properties);
//        new Thread(bind).start();

//        StackObjectPool<Session> ss = new StackObjectPool<Session>(new BindSessionFactory(properties));
//        StackObjectPoolFactory<Session> sf = new StackObjectPoolFactory<Session>(new BindSessionFactory(properties));
//        for (int i = 0; i < sf.getInitCapacity(); i++) {
//            logger.info("Adding to the pool ... Session Pool ID: " + i);
//            sf.createPool().addObject();
//        }
//        properties.setPool(sf.createPool());
        
        //session pool
        SessionPool pool = new SessionPool(properties);
        
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

        //enquire link thread        
        new Thread(new EnquireLinkThread()).start();
        
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
