package com.pet.parser.tcp;

public class ConnectionProperties {

    private String ip;
    private Integer port;


    public void setPort(Integer port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
