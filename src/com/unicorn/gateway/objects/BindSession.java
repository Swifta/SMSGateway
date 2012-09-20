/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.objects;

import com.logica.smpp.Session;

/**
 *
 * @author Opeyemi
 */
public class BindSession {
    
    private Session session;
    private boolean used;
    
    public BindSession(){
        
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }    
    
}
