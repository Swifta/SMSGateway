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
import org.apache.log4j.Logger;

/**
 *
 * @author Opeyemi
 */
public class LoadProperties {

    Logger logger = Logger.getLogger(LoadProperties.class);
    
    public String getGatewayPath(){
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            return "E:\\PropertyFiles\\gateway.properties";
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("sunos") >= 0) {
            return "/opt/agentportal/properties/gateway.properties";
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("nix") >= 0) {
            return "/opt/agentportal/properties/gateway.properties";
        }
        return null;        
    }
    
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
        logger.info("Setting parameters .... ");
        property = new GatewayProperties();

        property.setIpaddress(prop.getProperty("ip-address"));
        logger.info("IP-Address : "+property.getIpaddress());
        property.setPort(Integer.parseInt(prop.getProperty("port")));
        logger.info("Port : "+property.getPort());
        property.setPassword(prop.getProperty("password"));
        logger.info("Password : "+property.getPassword());

        AddressRange addrRange = new AddressRange();

        byte ton = this.getByteProperty(prop, "addr-ton", addrRange.getTon());
        byte npi = this.getByteProperty(prop, "addr-npi", addrRange.getNpi());

        logger.info("Ton : "+ton);
        logger.info("Npi : "+npi);
        
        addrRange.setTon(ton);
        addrRange.setNpi(npi);
        addrRange.setAddressRange(prop.getProperty("address-range"));
        property.setAddressRange(addrRange);

        Address sourceAddress = property.getSourceAddress();
        
        ton = getByteProperty(prop,"source-ton", sourceAddress.getTon());
        npi = getByteProperty(prop,"source-npi", sourceAddress.getNpi());
        String addr = prop.getProperty("source-address",
                sourceAddress.getAddress());
        setAddressParameter("source-address", sourceAddress, ton, npi, addr);

        property.setSourceAddress(sourceAddress);
        
        Address destAddress = property.getDestAddress();
        
        ton = getByteProperty(prop,"destination-ton", destAddress.getTon());
        npi = getByteProperty(prop,"destination-npi", destAddress.getNpi());
        addr = prop.getProperty("destination-address",
                destAddress.getAddress());
        setAddressParameter("destination-address", destAddress, ton, npi, addr);
        
        property.setDestAddress(destAddress);

        logger.info("Soutce Ton "+property.getSourceAddress().getTon());
        logger.info("Soutce Npi "+property.getSourceAddress().getNpi());

        logger.info("Dest Ton "+property.getDestAddress().getTon());
        logger.info("Dest Npi "+property.getDestAddress().getNpi());        
        
        property.setSystemId(prop.getProperty("system-id"));
        logger.info("System ID : "+property.getSystemId());
        property.setSystemType(prop.getProperty("system-type"));
        logger.info("System Type : "+property.getSystemType());
        property.setServiceType(prop.getProperty("service-type"));
        logger.info("Service Type : "+property.getServiceType());


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
        
        logger.info("Bind Mode : "+property.getBindOption());

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

        logger.info("Receive Timeout : "+property.getReceiveTimeout());
        
        String syncMode = prop.getProperty("sync-mode", (property.isAsynchronous() ? "async" : "sync"));
        if (syncMode.equalsIgnoreCase("sync")) {
            property.setAsynchronous(false);
        } else if (syncMode.equalsIgnoreCase("async")) {
            property.setAsynchronous(true);
        } else {
            property.setAsynchronous(false);
        }

        logger.info("Async : "+property.isAsynchronous());
        
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
