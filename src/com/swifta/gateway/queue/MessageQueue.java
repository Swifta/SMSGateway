/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.queue;

import com.unicorn.gateway.objects.Message;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class MessageQueue {

    public static ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(500);
    Logger logger = Logger.getLogger(MessageQueue.class);

    public MessageQueue() {
    }

    public boolean addToQueue(Message message) throws InterruptedException {
        if (!queue.add(message)) {
            queue.put(message);
            logger.info("Message added to queue .... ");
            return true;
        } else {
            return false;
        }
    }

    public Message getMessageFromQueue() throws InterruptedException {
        return MessageQueue.queue.take();
    }

    public void clearQueue() {
        MessageQueue.queue.clear();
    }
}
