/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.smpp.pool;

import com.logica.smpp.Session;
import com.unicorn.gateway.process.BindGateway;
import com.unicorn.gateway.process.GatewayProperties;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class SessionPool {
    
    public static ObjectPool<Session> sessionPool = new StackObjectPool<Session>();
    private Logger logger = Logger.getLogger(SessionPool.class);
    
    public SessionPool(GatewayProperties prop) {
        try {
            Session session = BindGateway.bindSessionGateway(prop);
            sessionPool.setFactory(new SMPPSessionPoolFactory(session));
            try {
                logger.info("Created a session, putting in a pool");
                logger.debug(session);
                session.enableStateChecking();
                sessionPool.addObject();
                logger.debug(sessionPool);
            } catch (Exception ex) {
                logger.error(ex);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
