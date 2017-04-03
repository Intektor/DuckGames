package de.intektor.duckgames.common.net.sockets;

import de.intektor.duckgames.common.net.AbstractServerSocket;
import de.intektor.duckgames.common.net.AbstractSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Intektor
 */
public class TCPServerSocketImpl implements AbstractServerSocket {

    private ServerSocket socket;

    public TCPServerSocketImpl(ServerSocket socket) {
        this.socket = socket;
    }

    public TCPServerSocketImpl(int port) throws IOException {
        socket = new ServerSocket(port);
    }

    @Override
    public AbstractSocket accept() throws IOException {
        Socket accept = socket.accept();
        return new TCPSocketImpl(accept);
    }

    @Override
    public void setSoTimeout(int i) throws SocketException {
        socket.setSoTimeout(i);
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public boolean isBound() {
        return socket.isBound();
    }
}
