package de.intektor.duckgames.desktop;

import de.intektor.duckgames.common.server.HostingType;
import de.intektor.duckgames.common.Networking;
import de.intektor.duckgames.common.net.AbstractServerSocket;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.sockets.TCPServerSocketImpl;
import de.intektor.duckgames.common.net.sockets.TCPSocketImpl;

import java.io.IOException;

/**
 * @author Intektor
 */
public class DesktopNetworkingImpl implements Networking {
    @Override
    public boolean isBluetoothAvailable() {
        return false;
    }

    @Override
    public AbstractServerSocket createServerSocket(HostingType hostingType, int port) throws IOException {
        if (hostingType == HostingType.LAN || hostingType == HostingType.INTERNET) {
            return new TCPServerSocketImpl(port);
        }
        return null;
    }

    @Override
    public AbstractSocket connect(HostingType hostingType, int port, String ip) throws IOException {
        if (hostingType == HostingType.LAN || hostingType == HostingType.INTERNET) {
            return new TCPSocketImpl(ip, port);
        }
        return null;
    }
}
