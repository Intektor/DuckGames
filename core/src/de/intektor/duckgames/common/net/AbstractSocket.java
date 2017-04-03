package de.intektor.duckgames.common.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * @author Intektor
 */
public interface AbstractSocket {

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    boolean isConnected();

    boolean isClosed();

    void close() throws IOException;

    InetAddress getInetAddress();
}
