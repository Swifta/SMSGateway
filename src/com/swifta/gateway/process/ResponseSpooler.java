/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.process;

import com.swifta.gateway.queue.ResponseQueue;
import com.unicorn.gateway.objects.Message;
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
