/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.unicorn.gateway.objects.Message;
import com.unicorn.gateway.queue.MessageQueue;
import com.unicorn.gateway.queue.ResponseQueue;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.json.me.JSONObject;

/**
 *
 * @author Opeyemi
 */
public class ServerProcess implements Runnable {

    Logger logger = Logger.getLogger(ServerProcess.class);
    private ServerSocket socket;
    private MessageQueue queue;
    private ResponseQueue rQueue;
    private GatewayProperties prop;

    public ServerProcess(ServerSocket server, MessageQueue queue, ResponseQueue rQueue, GatewayProperties prop) {
        this.socket = server;
        this.queue = queue;
        this.rQueue = rQueue;
        this.prop = prop;
    }

    private void connectAndListen() throws Exception {
        logger.info("Waiting for connections .... ");
        Socket sock = this.socket.accept();
        sock.setSoTimeout(30000);
        logger.info("Creating a socket on port " + sock.getPort()+" with timeout "+sock.getSoTimeout());

        //logger.info("Bind state : "+BindGateway.session.isBound());

        //reader
        DataInputStream din = new DataInputStream(sock.getInputStream());
        String m = din.readUTF();

        //put message on queue
        Message msg = new Message().fromJson(m);
        msg.setMessageID(sock.getPort());
        logger.info("Putting " + new Message().toJson(msg) + " on queue ... ");
        //this.queue.addToQueue(new Message().fromJson(m));

        SubmitMessage sb = new SubmitMessage(prop.getPool());
        boolean sent = sb.submitMessage(msg.getMessage(), msg.getDestination(), prop);

        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

        if (sent) {
            JSONObject js = new JSONObject();

            js.put("bound", true);
            js.put("sent", true);

            dos.writeUTF(js.toString());
            logger.info("Response back to client : " + js.toString());
        } else {
            JSONObject js = new JSONObject();

            js.put("bound", false);
            js.put("sent", false);

            dos.writeUTF(js.toString());

            logger.info("Response back to client : " + js.toString());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                connectAndListen();
            } catch (Exception ex) {
                BindThread.bound = false;
                BindThread.running = false;
                
                logger.info("Starting to bind ... Please wait.");
                //start binding again
                if (BindThread.running == false) {
                    BindThread bind = new BindThread(prop);
                    new Thread(bind).start();
                }
                logger.error(ex.getMessage());
            }
        }
    }
}
