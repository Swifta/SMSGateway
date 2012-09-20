/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.pdu.EnquireLink;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class Ping {

    private static Logger logger = Logger.getLogger(Ping.class);

    public static boolean ping(GatewayProperties prop) {
        logger.info("Pinging the gateway .... ");
        SubmitMessage m = new SubmitMessage(prop.getPool());
        try {
            EnquireLink request = new EnquireLink();
            logger.info(request.debugString());
            BindGateway.session.enquireLink(request);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            
            new UnbindGateway(prop).unbind();
            
            BindThread.bound = false;
            BindThread.running = false;
            BindGateway.session = null;            
            
            BindThread bind = new BindThread(prop);
            new Thread(bind).start();
            
            return false;
        }
    }
}
