package de.intektor.duckgames.common.net.sockets;

import de.intektor.duckgames.common.net.AbstractSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Intektor
 */
public class TCPSocketImpl implements AbstractSocket {

    private Socket socket;

    public TCPSocketImpl(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
    }

    public TCPSocketImpl(Socket socket) {
        this.socket = socket;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }
}
