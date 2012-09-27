/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swifta.smpp.pool;

import com.logica.smpp.Session;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class SMPPSessionPoolFactory extends BasePoolableObjectFactory<Session> {

    private Session session;
    private Logger logger = Logger.getLogger(SMPPSessionPoolFactory.class);

    public SMPPSessionPoolFactory(Session session) {
        this.session = session;
    }

    @Override
    public Session makeObject() throws Exception {
        logger.info("Making session");
        return session;
    }
}
