/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.Session;
import com.logica.smpp.pdu.EnquireLinkResp;
import com.unicorn.smpp.pool.SessionPool;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class EnquireLinkThread implements Runnable {

    public EnquireLinkThread() {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Logger.getLogger(EnquireLinkThread.class).debug("Pulling session out of pool ");
                Session sess = SessionPool.sessionPool.borrowObject();
                Logger.getLogger(EnquireLinkThread.class).debug("Enquiring link " + sess);
                EnquireLinkResp resp = sess.enquireLink();
                Logger.getLogger(EnquireLinkThread.class).debug("Returning session to pool ");
                SessionPool.sessionPool.returnObject(sess);
                Logger.getLogger(EnquireLinkThread.class).debug("Returned ... ");
                Logger.getLogger(EnquireLinkThread.class).debug(resp);
            } catch (Exception ex) {
                Logger.getLogger(EnquireLinkThread.class).error(ex);
            }
            Pause.pause(3000);
        }
    }
}
