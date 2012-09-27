/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.queue;

import com.unicorn.gateway.objects.Message;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Opeyemi
 */
public class ResponseQueue {
    
    public static ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(500);
    
    
    public boolean addToQueue(Message message) throws InterruptedException {
        if (!queue.add(message)) {
            queue.put(message);
            return true;
        } else {
            return false;
        }
    }

    public Message getMessageFromQueue() throws InterruptedException {
        return ResponseQueue.queue.take();
    }

    public void clearQueue() {
        ResponseQueue.queue.clear();
    }

    public static ArrayBlockingQueue<Message> getQueue() {
        return queue;
    }
    
}
