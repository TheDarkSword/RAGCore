package it.revarmygaming.commonapi.socket.server;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public final class SSLSocketServer implements Runnable {

    private SSLServerSocket serverSocket;
    private int port;
    private int bufferSize;
    private Thread thread;

    private List<Connection> connections;

    public SSLSocketServer(int port){
        this.port = port;
        connections = new ArrayList<>();
        thread = new Thread(this);
    }

    public void start(int bufferSize, File keyStore, String password) throws IOException {
        this.bufferSize = bufferSize;
        System.setProperty("javax.net.ssl.keyStore", keyStore.toString());
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");

        serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket();
        serverSocket.setReceiveBufferSize(bufferSize);
        serverSocket.bind(new InetSocketAddress(port));
        thread.start();
    }

    public void start(File keyStore, String password) throws IOException {
        start(65536, keyStore, password);
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
