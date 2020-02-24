package it.revarmygaming.commonapi.socket.packet;

public class PacketException extends Exception {

    public PacketException() {
        super();
    }

    public PacketException(String message){
        super(message);
    }

    public PacketException(String message, Throwable cause){
        super(message, cause);
    }

    public PacketException(Throwable cause){
        super(cause);
    }
}
