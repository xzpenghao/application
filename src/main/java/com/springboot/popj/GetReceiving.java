package com.springboot.popj;

public class GetReceiving {

    private  String slbh;
    private  String messageType;
    private  String modelId;
    private  String userCodeYCSL;

    public String getUserCodeYCSL() {
        return userCodeYCSL;
    }

    public void setUserCodeYCSL(String userCodeYCSL) {
        this.userCodeYCSL = userCodeYCSL;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
