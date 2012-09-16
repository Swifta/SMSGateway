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
import org.json.me.JSONObject;

/**
 *
 * @author Opeyemi
 */
public class ServerProcess implements Runnable {

    private ServerSocket socket;
    private MessageQueue queue;
    private ResponseQueue rQueue;

    public ServerProcess(ServerSocket server, MessageQueue queue, ResponseQueue rQueue) {
        this.socket = server;
        this.queue = queue;
        this.rQueue = rQueue;
    }

    private void connectAndListen() throws Exception {
        System.out.println("Waiting for connections .... ");
        Socket sock = this.socket.accept();
        System.out.println("Creating a socket on port " + sock.getPort());

        //reader
        DataInputStream din = new DataInputStream(sock.getInputStream());
        String m = din.readUTF();

        //put message on queue
        Message msg = new Message().fromJson(m);
        msg.setMessageID(sock.getPort());
        System.out.println("Putting " + new Message().toJson(msg) + " on queue ... ");
        this.queue.addToQueue(new Message().fromJson(m));

        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
        
        if (BindThread.bound) {
            JSONObject js = new JSONObject();

            js.put("bound", true);
            js.put("sent", true);

            dos.writeUTF(js.toString());
        } else {
            JSONObject js = new JSONObject();

            js.put("bound", false);
            js.put("sent", false);

            dos.writeUTF(js.toString());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                connectAndListen();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
