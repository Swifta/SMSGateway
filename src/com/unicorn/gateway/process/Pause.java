/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Opeyemi
 */
public class Pause {

    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pause.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
