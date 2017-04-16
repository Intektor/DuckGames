package de.intektor.duckgames.common;

import de.intektor.duckgames.common.net.AbstractServerSocket;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.server.HostingType;

import java.io.IOException;

/**
 * @author Intektor
 */
public interface Networking {

    boolean isBluetoothAvailable();

    AbstractServerSocket createServerSocket(HostingType hostingType, int port) throws IOException;

    AbstractSocket connect(HostingType hostingType, int port, String ip) throws IOException;
}
