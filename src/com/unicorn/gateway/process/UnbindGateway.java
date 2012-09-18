/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.pdu.UnbindResp;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class UnbindGateway {

    private Logger logger = Logger.getLogger(UnbindGateway.class);

    public void unbind() {

        try {
            if (!BindThread.bound) {
                logger.info("Not bound, cannot unbind.");
                return;
            }

            // send the request
            logger.info("Going to unbind.");
            if (BindGateway.session.getReceiver().isReceiver()) {
                logger.info("It can take a while to stop the receiver.");
            }
            UnbindResp response = BindGateway.session.unbind();
            BindGateway.session.close();
            logger.info("Unbind response " + response.debugString());
            BindThread.bound = false;
            logger.info("Bind state : "+BindGateway.session.isBound());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
