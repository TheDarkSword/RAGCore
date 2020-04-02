package it.revarmygaming.commonapi.redis;

import java.io.Serializable;

public class RedisMessage implements Serializable {

    private String source;
    private String destination;
    private String dataType;
    private Object data;

    public RedisMessage(String source, String destination, Object data) {
        this.source = source;
        this.destination = destination;
        this.data = data;
        this.dataType = data.getClass().getName();
    }

    /**
     * Return the source name.
     */
    public String getSource() {
        return source;
    }

    /**
     * Return the destination name.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Return the class name of the data contained in this packet.
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Return the data contained in this message.
     */
    public Object getData() {
        return data;
    }
}
