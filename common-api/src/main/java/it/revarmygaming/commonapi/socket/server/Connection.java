package it.revarmygaming.commonapi.socket.server;

import it.revarmygaming.commonapi.socket.Buffer;
import it.revarmygaming.commonapi.socket.packet.Packet;
import it.revarmygaming.commonapi.socket.packet.PacketException;
import it.revarmygaming.commonapi.socket.packet.Packets;

import java.io.*;
import java.net.Socket;

public final class Connection implements Runnable {

    private Socket socket;
    private Buffer buffer;
    private Thread thread;

    public Connection(Socket socket){
        this.socket = socket;
        thread = new Thread(this);
    }

    public Connection accept(int bufferSize) throws IOException {
        socket.setReceiveBufferSize(bufferSize);
        socket.setSendBufferSize(bufferSize);
        buffer = new Buffer(socket, new DataInputStream(new BufferedInputStream(socket.getInputStream(), bufferSize)),
                new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), bufferSize)));
        thread.start();
        return this;
    }

    public Connection accept() throws IOException {
        return accept(65536);
    }

    @Override
    public void run(){
        while(true){
            Integer id = buffer.readInt();

            if(id == null) break;
            Class packetClass = Packets.getPacket(id);

            if(packetClass == null){
                try {
                    throw new PacketException("Not existing packet");
                } catch (PacketException e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                Packet packet = (Packet) packetClass.newInstance();
                packet.decode(buffer);
                Packets.callProcess(packet, buffer);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        socket.close();
        buffer.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public Thread getThread() {
        return thread;
    }
}
