/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.queue;

import com.swifta.gateway.process.BindThread;
import com.swifta.gateway.process.GatewayProperties;
import com.swifta.gateway.process.SubmitMessage;
import com.unicorn.gateway.objects.Message;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class MessageProcessor implements Runnable {

    private int id;
    private GatewayProperties properties;
    Logger logger = Logger.getLogger(MessageProcessor.class);

    public MessageProcessor(int id, GatewayProperties properties) {
        this.id = id;
        this.properties = properties;
        logger.info("Starting a Message Processor with id " + id);
    }

    private void process() throws Exception {

        Message message = MessageQueue.queue.take();
        if (message != null) {
            SubmitMessage submitMessage = new SubmitMessage(properties.getPool());

            if (submitMessage.submitMessage(message.getMessage(), message.getDestination(), this.properties)) {
                logger.info("Message has been delivered to " + message.getDestination());
                ResponseQueue.queue.put(message);
                logger.info("Message with id " + this.id + " placed on response queue");
            } else {
                logger.info("Unable to deliver message " + message.getMessage() + " to " + message.getDestination());
                BindThread.bound = false;
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                process();
            }
        } catch (Exception ex) {
            BindThread.bound = false;
            BindThread.running = false;
            //start binding again
            if (BindThread.running == false) {
                BindThread bind = new BindThread(properties);
                new Thread(bind).start();
            }
            logger.error(ex.getMessage());
        }
    }
}
