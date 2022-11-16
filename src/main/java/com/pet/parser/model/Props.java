package com.pet.parser.model;


/**
 * An entity class, it has two fields - <i>String ip</i> and <i>Integer port</i>.
 */
public class Props {

    private String ip;
    private Integer port;

    /**
     * A standard setter for <i>payload</i> field.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * A standard setter for <i>port</i> field.
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * A standard getter for <i>ip</i> field.
     *
     * @return String ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * A standard getter for <i>port</i> field.
     *
     * @return Integer port
     */
    public Integer getPort() {
        return port;
    }
}
