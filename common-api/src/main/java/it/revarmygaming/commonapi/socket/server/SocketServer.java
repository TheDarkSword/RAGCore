package it.revarmygaming.commonapi.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public final class SocketServer implements Runnable {

    private ServerSocket serverSocket;
    private int port;
    private int bufferSize;
    private Thread thread;

    private List<Connection> connections;

    public SocketServer(int port){
        this.port = port;
        connections = new ArrayList<>();
        thread = new Thread(this);
    }

    public void start(int bufferSize) throws IOException {
        this.bufferSize = bufferSize;
        serverSocket = new ServerSocket();
        serverSocket.setReceiveBufferSize(bufferSize);
        serverSocket.bind(new InetSocketAddress(port));
        thread.start();
    }

    public void start() throws IOException {
        start(65536);
    }

    @Override
    public void run(){
        while(true){
            try {
                connections.add(new Connection(serverSocket.accept()).accept(bufferSize));
            } catch (IOException e) {
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
        serverSocket.close();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
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

    public List<Connection> getConnections() {
        return connections;
    }
}
