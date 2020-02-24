package it.revarmygaming.commonapi.socket.packet;

import it.revarmygaming.commonapi.socket.Buffer;

import java.io.*;

public class RawPacket implements Packet {

    public RawPacket(){

    }

    private Object object;

    public RawPacket(Object object){
       this.object = object;
    }

    public void encode(Buffer buffer) {
        buffer.writeInt(0x0); //Packet ID

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            byte[] obj = bos.toByteArray();
            buffer.writeInt(obj.length);
            buffer.writeBytes(obj);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(out != null) out.close();
                bos.close();
            } catch (IOException ignored) {

            }
        }
    }

    public Packet decode(Buffer buffer) {
        int length = buffer.readInt();
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer.readBytes(length));
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            object = in.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            try {
                if(in != null) in.close();
                bis.close();
            } catch (IOException ignored){

            }
        }
        return this;
    }

    public Object getObject() {
        return object;
    }
}
