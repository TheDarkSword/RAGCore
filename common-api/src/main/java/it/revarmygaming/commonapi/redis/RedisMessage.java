package it.revarmygaming.commonapi.redis;

import it.revarmygaming.commonapi.redis.messanger.OutgoingMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.UUID;

public class RedisMessage implements OutgoingMessage {

    private final UUID id;
    private final String action;
    private final Serializable data;
    private final String dataType;

    public RedisMessage(String action, Serializable data){
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
}
