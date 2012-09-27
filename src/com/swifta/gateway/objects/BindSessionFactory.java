/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.objects;

import com.logica.smpp.Data;
import com.logica.smpp.Session;
import com.logica.smpp.TCPIPConnection;
import com.logica.smpp.pdu.*;
import com.swifta.gateway.listener.SMPPTestPDUEventListener;
import com.swifta.gateway.process.GatewayProperties;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class BindSessionFactory extends BasePoolableObjectFactory<Session> {

    private GatewayProperties prop;
    Logger logger = Logger.getLogger(BindSessionFactory.class);
    private boolean bindStatus = false;
    private Session bindSession = null;

    public BindSessionFactory(GatewayProperties prop) {
        this.prop = prop;
    }

    public Session makeSession() {
        Session session = null;
        SMPPTestPDUEventListener pduListener = null;
        BindRequest request = null;
        BindResponse response = null;

        while (bindStatus == false) {
            try {
                logger.info("Trying to bind to smpp ... ");
                if (prop.getBindOption().compareToIgnoreCase("t") == 0) {
                    request = new BindTransmitter();
                } else if (prop.getBindOption().compareToIgnoreCase("r") == 0) {
                    request = new BindReceiver();
                } else if (prop.getBindOption().compareToIgnoreCase("tr") == 0) {
                    request = new BindTransciever();
                } else {
                    logger.info("Invalid bind mode, expected t, r or tr, got "
                            + prop.getBindOption() + ". Operation canceled.");
                }


                logger.info("Connecting to " + prop.getIpaddress() + " and port " + prop.getPort());
                TCPIPConnection connection = new TCPIPConnection(prop.getIpaddress(), prop.getPort());
                connection.setReceiveTimeout(60 * 1000);
                session = new Session(connection);

                logger.info("Connection : " + connection.toString());

                //logger.info("... Setting default values .... ");
                // set values
                request.setSystemId(prop.getSystemId());
                request.setPassword(prop.getPassword());
                request.setSystemType(prop.getSystemType());
                request.setInterfaceVersion((byte) 0x34);
                request.setAddressRange(prop.getAddressRange());


                logger.info(request.getAddressRange().toString());
                logger.info(request.getSystemId());
                logger.info(request.getSystemType());
                logger.info(request.getPassword());
                logger.info("Bind Option : " + prop.getBindOption());

                logger.info("Request : " + request.debugString());

                // send the request
                if (prop.isAsynchronous()) {
                    pduListener = new SMPPTestPDUEventListener(session);
                    logger.info("Asychronous binding ... Please wait ... ");
                    response = session.bind(request, pduListener);
                } else {
                    logger.info("Synchronous binding ... Please wait ... ");
                    response = session.bind(request);
                }

                logger.info("Response : " + response.debugString());

                if (response.getCommandStatus() == Data.ESME_ROK) {
                    logger.info("Binding to sms gateway was successful ... ");
                    bindStatus = true;
                    return session;
                } else {
                    logger.info("Binding to sms gateway unsuccessful ... ");
                    bindStatus = false;
                    return session;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return session;
    }

    @Override
    public Session makeObject() throws Exception {
        return makeSession();
    }

    @Override
    public void passivateObject(Session session) {
        try {
            session.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
