/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.queue;

import com.unicorn.gateway.objects.Message;
import com.unicorn.gateway.process.BindThread;
import com.unicorn.gateway.process.GatewayProperties;
import com.unicorn.gateway.process.SubmitMessage;
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
            SubmitMessage submitMessage = new SubmitMessage();

            if (submitMessage.submitMessage(message.getMessage(), message.getDestination(), this.properties)) {
                logger.info("Message has been delivered to " + message.getDestination());
                ResponseQueue.queue.put(message);
                logger.info("Message with id " + this.id + " placed on response queue");
            } else {
                logger.info("Unable to deliver message " + message.getMessage() + " to " + message.getDestination());
                BindThread.bound = false;

                //start binding again
                if (BindThread.running == false) {
                    BindThread bind = new BindThread(properties);
                    new Thread(bind).start();
                }
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
            logger.error(ex.getMessage());
        }
    }
}
