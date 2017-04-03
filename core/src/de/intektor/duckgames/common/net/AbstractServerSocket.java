package de.intektor.duckgames.common.net;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author Intektor
 */
public interface AbstractServerSocket {

    AbstractSocket accept() throws IOException;

    void setSoTimeout(int i) throws SocketException;

    int getLocalPort();

    void close() throws IOException;

    boolean isBound();
}
