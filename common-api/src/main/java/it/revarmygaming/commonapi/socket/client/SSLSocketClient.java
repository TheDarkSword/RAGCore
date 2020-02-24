package it.revarmygaming.commonapi.socket.client;

import it.revarmygaming.commonapi.socket.Buffer;
import it.revarmygaming.commonapi.socket.packet.Packet;
import it.revarmygaming.commonapi.socket.packet.PacketException;
import it.revarmygaming.commonapi.socket.packet.Packets;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class SSLSocketClient implements Runnable {

    private SSLSocket socket;
    private Buffer buffer;
    private String host;
    private int port;
    private int bufferSize;
    private Thread thread;

    public SSLSocketClient(String host, int port){
        this.host = host;
        this.port = port;
        thread = new Thread(this);
    }

    public void connect(int bufferSize, File keyStore, String password) throws IOException {
        this.bufferSize = bufferSize;
        System.setProperty("javax.net.ssl.trustStore", keyStore.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", password);

        socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.setReceiveBufferSize(bufferSize);
        socket.setSendBufferSize(bufferSize);
        socket.connect(new InetSocketAddress(InetAddress.getByName(host), port));
        socket.startHandshake();
        buffer = new Buffer(socket, new DataInputStream(new BufferedInputStream(socket.getInputStream(), bufferSize)),
                new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), bufferSize)));
        thread.start();
    }

    public void connect(File keyStore, String password) throws IOException {
        connect(65536, keyStore, password);
    }

    @Override
    public void run(){
        while(true){
            int id = buffer.readInt();

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

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public Thread getThread() {
        return thread;
    }
}
