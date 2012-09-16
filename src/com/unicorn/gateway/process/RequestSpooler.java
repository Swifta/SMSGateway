/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.unicorn.gateway.queue.MessageProcessor;

/**
 *
 * @author Opeyemi
 */
public class RequestSpooler {
    
    private GatewayProperties properties;
    
    public RequestSpooler(GatewayProperties properties){
        this.properties = properties;
    }
    
    
    public void spool(){
        for(int i = 0; i < 5;i++){
            new Thread(new MessageProcessor(i,properties)).start();
        }
    }
    
}
