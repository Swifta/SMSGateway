/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.Data;
import com.logica.smpp.pdu.Address;
import com.logica.smpp.pdu.AddressRange;
import com.logica.smpp.pdu.WrongLengthOfStringException;
import java.util.Properties;
import main.PropertyFileReader;

/**
 *
 * @author Opeyemi
 */
public class LoadProperties {

    public String getPropertiesFilePath() {
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            return "E:\\PropertyFiles\\smpptestportal.properties";
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("sunos") >= 0) {
            return "/opt/agentportal/properties/smpptestportal.properties";
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("nix") >= 0) {
            return "/opt/agentportal/properties/smpptestportal.properties";
        }
        return null;
    }

    public GatewayProperties loadProperties(String fileName) throws Exception {
        GatewayProperties property = null;

        PropertyFileReader reader = new PropertyFileReader(fileName);
        Properties prop = reader.getAllProperties();

        property = new GatewayProperties();

        property.setIpaddress(prop.getProperty("ip-address"));
        property.setPort(Integer.parseInt(prop.getProperty("port")));
        property.setPassword(prop.getProperty("password"));

        AddressRange addrRange = new AddressRange();

        byte ton = this.getByteProperty(prop, "addr-ton", addrRange.getTon());
        byte npi = this.getByteProperty(prop, "addr-npi", addrRange.getNpi());

        addrRange.setTon(ton);
        addrRange.setNpi(npi);
        addrRange.setAddressRange(prop.getProperty("address-range"));

        property.setAddressRange(addrRange);

        property.getSourceAddress().setTon(this.getByteProperty(prop, "source-ton", property.getSourceAddress().getTon()));
        property.getSourceAddress().setNpi(this.getByteProperty(prop, "source-npi", property.getSourceAddress().getNpi()));
        property.getSourceAddress().setAddress(prop.getProperty("source-address"));


        property.getDestAddress().setTon(this.getByteProperty(prop, "destination-ton", property.getDestAddress().getTon()));
        property.getDestAddress().setNpi(this.getByteProperty(prop, "destination-npi", property.getDestAddress().getTon()));
        property.getDestAddress().setAddress(prop.getProperty("destination-address"));


        property.setSystemId(prop.getProperty("system-id"));
        property.setSystemType(prop.getProperty("system-type"));
        property.setServiceType(prop.getProperty("service-type"));


        String bindMode = prop.getProperty("bind-mode");
        if (bindMode.equalsIgnoreCase("transmitter")) {
            bindMode = "t";
        } else if (bindMode.equalsIgnoreCase("receiver")) {
            bindMode = "r";
        } else if (bindMode.equalsIgnoreCase("transciever")) {
            bindMode = "tr";
        } else if (!bindMode.equalsIgnoreCase("t")
                && !bindMode.equalsIgnoreCase("r")
                && !bindMode.equalsIgnoreCase("tr")) {
            System.out.println("The value of bind-mode parameter in "
                    + "the configuration file " + fileName + " is wrong. "
                    + "Setting the default");
            bindMode = "t";
        }

        property.setBindOption(bindMode);

        int rcvTimeout = 0;
        if (property.getReceiveTimeout() == Data.RECEIVE_BLOCKING) {
            rcvTimeout = -1;
        } else {
            rcvTimeout = ((int) property.getReceiveTimeout()) / 1000;
        }

        // receive timeout in the cfg file is in seconds, we need milliseconds
        // also conversion from -1 which indicates infinite blocking
        // in the cfg file to Data.RECEIVE_BLOCKING which indicates infinite
        // blocking in the library is needed.        

        rcvTimeout = Integer.parseInt(String.valueOf(prop.getProperty("receive-timeout")));

        if (rcvTimeout == -1) {
            property.setReceiveTimeout(Data.RECEIVE_BLOCKING);
        } else {
            property.setReceiveTimeout(rcvTimeout * 1000);
        }

        String syncMode = prop.getProperty("sync-mode", (property.isAsynchronous() ? "async" : "sync"));
        if (syncMode.equalsIgnoreCase("sync")) {
            property.setAsynchronous(false);
        } else if (syncMode.equalsIgnoreCase("async")) {
            property.setAsynchronous(true);
        } else {
            property.setAsynchronous(false);
        }

        return property;
    }

    /**
     * Gets a property and converts it into byte.
     */
    private byte getByteProperty(Properties properties, String propName, byte defaultValue) {
        return Byte.parseByte(properties.getProperty(propName, Byte.toString(defaultValue)));
    }

    /**
     * Gets a property and converts it into integer.
     */
    private int getIntProperty(Properties properties, String propName, int defaultValue) {
        return Integer.parseInt(properties.getProperty(propName, Integer.toString(defaultValue)));
    }

    /**
     * Sets attributes of
     * <code>Address</code> to the provided values.
     */
    private void setAddressParameter(String descr, Address address, byte ton, byte npi, String addr) {
        address.setTon(ton);
        address.setNpi(npi);
        try {
            address.setAddress(addr);
        } catch (WrongLengthOfStringException e) {
            System.out.println("The length of " + descr + " parameter is wrong.");
        }
    }
}
