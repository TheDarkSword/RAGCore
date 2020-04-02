package it.revarmygaming.commonapi.redis;

import it.revarmygaming.commonapi.redis.messanger.OutgoingMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.UUID;

public class RedisMessage implements OutgoingMessage, Serializable {

    private UUID id;
    private String action;
    private Object data;
    private String dataType;

    public RedisMessage(String action, Object data){
        id = UUID.randomUUID();
        this.action = action;
        this.data = data;
        dataType = data.getClass().getName();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public Object getData(){
        return data;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public String asEncodedString() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
