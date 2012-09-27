/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.gateway.process;

import com.logica.smpp.Data;
import com.logica.smpp.Session;
import com.logica.smpp.TCPIPConnection;
import com.logica.smpp.pdu.*;
import com.swifta.gateway.listener.SMPPTestPDUEventListener;
import com.unicorn.gateway.process.Pause;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class BindGateway {

    //variables
    static Session session = null;
    static SMPPTestPDUEventListener pduListener = null;
    private static Logger logger = Logger.getLogger(BindGateway.class);
//    private ObjectPool<Session> pool = null;
//
//    public BindGateway(ObjectPool<Session> pool){
//        this.pool = pool;
//    }

    public boolean bindGateway(GatewayProperties prop) throws Exception {

        //start binding
        logger.info("Starting binding process .... ");

        BindRequest request = null;
        BindResponse response = null;

        if (prop.getBindOption().compareToIgnoreCase("t") == 0) {
            request = new BindTransmitter();
        } else if (prop.getBindOption().compareToIgnoreCase("r") == 0) {
            request = new BindReceiver();
        } else if (prop.getBindOption().compareToIgnoreCase("tr") == 0) {
            request = new BindTransciever();
        } else {
            logger.info("Invalid bind mode, expected t, r or tr, got "
                    + prop.getBindOption() + ". Operation canceled.");
            return false;
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
            BindThread.bound = true;
            return true;
        } else {
            logger.info("Binding to sms gateway unsuccessful ... ");
            return false;
        }
    }

    public static Session bindSessionGateway(GatewayProperties prop) throws Exception {
        //start binding
        while (!BindThread.bound) {
            logger.info("Starting binding process .... ");

            BindRequest request = null;
            BindResponse response = null;

            if (prop.getBindOption().compareToIgnoreCase("t") == 0) {
                request = new BindTransmitter();
            } else if (prop.getBindOption().compareToIgnoreCase("r") == 0) {
                request = new BindReceiver();
            } else if (prop.getBindOption().compareToIgnoreCase("tr") == 0) {
                request = new BindTransciever();
            } else {
                logger.info("Invalid bind mode, expected t, r or tr, got "
                        + prop.getBindOption() + ". Operation canceled.");
                return null;
            }


            logger.info("Connecting to " + prop.getIpaddress() + " and port " + prop.getPort());
            TCPIPConnection connection = new TCPIPConnection(prop.getIpaddress(), prop.getPort());
            connection.setReceiveTimeout(60 * 1000);
            session = new Session(connection);

            
            logger.debug(session);
            logger.debug(connection);

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

            logger.debug(response);

            if (response.getCommandStatus() == Data.ESME_ROK) {
                logger.info("Binding to sms gateway was successful ... ");
                BindThread.bound = true;
            } else {
                logger.info("Binding to sms gateway unsuccessful ... ");
            }
            Pause.pause(2000);
        }
        return session;
    }
}
