/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicorn.gateway.process;

import com.logica.smpp.Data;
import com.logica.smpp.pdu.Address;
import com.logica.smpp.pdu.AddressRange;

/**
 *
 * @author Opeyemi
 */
public class GatewayProperties {
    
    private String systemId = null;
    private String password = null;
    private String bindOption = "t";
    private boolean asynchronous = false;
    private String systemType = "";
    private String serviceType = "";
    private byte esmClass = 0;
    private byte protocolId = 0;
    private byte priorityFlag = 0;
    private byte registeredDelivery = 0;
    private byte replaceIfPresentFlag = 0;
    private byte dataCoding = 0;
    private byte smDefaultMsgId = 0;
    private long receiveTimeout = Data.RECEIVE_BLOCKING;
    private String scheduleDeliveryTime = "";
    private String validityPeriod = "";
    private int numberOfDestination = 1;
    private String messageId = "";
    private String ipaddress;
    private int port;
    private AddressRange addressRange;
    Address sourceAddress = new Address();
    Address destAddress = new Address();

    public Address getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(Address destAddress) {
        this.destAddress = destAddress;
    }

    public Address getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(Address sourceAddress) {
        this.sourceAddress = sourceAddress;
    }    
    
    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public AddressRange getAddressRange() {
        return addressRange;
    }

    public void setAddressRange(AddressRange addressRange) {
        this.addressRange = addressRange;
    }    
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public String getBindOption() {
        return bindOption;
    }

    public void setBindOption(String bindOption) {
        this.bindOption = bindOption;
    }

    public byte getDataCoding() {
        return dataCoding;
    }

    public void setDataCoding(byte dataCoding) {
        this.dataCoding = dataCoding;
    }

    public byte getEsmClass() {
        return esmClass;
    }

    public void setEsmClass(byte esmClass) {
        this.esmClass = esmClass;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getNumberOfDestination() {
        return numberOfDestination;
    }

    public void setNumberOfDestination(int numberOfDestination) {
        this.numberOfDestination = numberOfDestination;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getPriorityFlag() {
        return priorityFlag;
    }

    public void setPriorityFlag(byte priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    public byte getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(byte protocolId) {
        this.protocolId = protocolId;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public byte getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    public void setReplaceIfPresentFlag(byte replaceIfPresentFlag) {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }

    public String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }    
    
    
}
