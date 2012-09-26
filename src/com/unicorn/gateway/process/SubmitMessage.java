/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.Session;
import com.logica.smpp.pdu.SubmitSM;
import com.logica.smpp.pdu.SubmitSMResp;
import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class SubmitMessage {

    private SubmitSM request = new SubmitSM();
    private SubmitSMResp response = new SubmitSMResp();
    Logger logger = Logger.getLogger(SubmitMessage.class);
    private ObjectPool<Session> pool;

    public SubmitMessage(ObjectPool<Session> pool) {
        this.pool = pool;
    }

    public boolean submitMessage(String message, String destination, GatewayProperties properties) throws Exception {
        Session session = null;
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
            if (response.isOk()) {
                return true;
            } else {
                return false;
            }
        } else {
            logger.info("Submitting request : " + request.toString());
            response = BindGateway.session.submit(request);
            if (response.isOk()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
