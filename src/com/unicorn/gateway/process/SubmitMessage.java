/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.pdu.SubmitSM;
import com.logica.smpp.pdu.SubmitSMResp;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class SubmitMessage {

    private SubmitSM request = new SubmitSM();
    private SubmitSMResp response = new SubmitSMResp();
    Logger logger = Logger.getLogger(SubmitMessage.class);

    public boolean submitMessage(String message, String destination, GatewayProperties properties) throws Exception {
        if (BindThread.bound) {
            logger.info("Preparing requests ... ");
            
            // set values
            request.setServiceType(properties.getServiceType());
            request.setSourceAddr(properties.getSourceAddress());
            request.setDestAddr(destination);
            request.setReplaceIfPresentFlag(properties.getReplaceIfPresentFlag());
            request.setShortMessage(message);
            request.setScheduleDeliveryTime(properties.getScheduleDeliveryTime());
            request.setValidityPeriod(properties.getValidityPeriod());
            request.setEsmClass(properties.getEsmClass());
            request.setProtocolId(properties.getProtocolId());
            request.setPriorityFlag(properties.getPriorityFlag());
            request.setRegisteredDelivery(properties.getRegisteredDelivery());
            request.setDataCoding(properties.getDataCoding());
            request.setSmDefaultMsgId(properties.getSmDefaultMsgId());

            // send the request
            request.assignSequenceNumber(true);

            logger.info("SMS request prepared ... ");

            if (properties.isAsynchronous()) {
                logger.info("Submitting request : " + request.toString());
                response = BindGateway.session.submit(request);
                return true;
            } else {
                logger.info("Submitting request : " + request.toString());
                response = BindGateway.session.submit(request);
                return true;
            }
        } else {
            logger.info("Unable to process request ... Gateway not bound");
            return false;
        }
    }
}
