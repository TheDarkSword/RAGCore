package it.revarmygaming.commonapi.socket.packet;

import it.revarmygaming.commonapi.socket.Buffer;

public interface Packet {

    void encode(Buffer buffer);

    Packet decode(Buffer buffer);
}
