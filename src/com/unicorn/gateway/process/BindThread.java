/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class BindThread implements Runnable {
    
    public static boolean bound,running = false;
    private GatewayProperties properties;
    Logger logger = Logger.getLogger(BindThread.class);
    
    public BindThread(GatewayProperties properties) {
        this.properties = properties;
    }
    
    @Override
    public void run() {
        while(BindThread.bound == false){
            BindThread.running = true;
            try {
                BindGateway bg = new BindGateway();
                bg.bindGateway(properties);
                Pause.pause(5000);
                //logger.info("Bind status : "+BindThread.bound);
                if(BindThread.bound == false)
                //logger.info("Starting binding process all over again in 5 seconds");
                Pause.pause(5000);
            } catch (Exception ex) {
                //logger.info("Bind status : "+BindThread.bound);
                //logger.info("Starting binding process all over again ");
                Pause.pause(5000);
                logger.error(ex.getMessage());
                ex.printStackTrace();
            }
        }
        BindThread.running = false;
    }
}
