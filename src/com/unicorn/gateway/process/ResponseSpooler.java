/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.unicorn.gateway.objects.Message;
import com.unicorn.gateway.queue.ResponseQueue;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author Opeyemi
 */
public class ResponseSpooler {

    private ResponseQueue queue;

    public ResponseSpooler(ResponseQueue queue) {
        this.queue = queue;
    }

    public void spool() throws Exception {
        Message m = ResponseQueue.queue.take();
        
        Socket socket = new Socket("127.0.0.1", m.getMessageID());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(new Message().toJson(m));
        dos.flush();
        dos.close();
    }
}
