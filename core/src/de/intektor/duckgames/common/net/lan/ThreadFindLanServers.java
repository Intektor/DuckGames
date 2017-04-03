package de.intektor.duckgames.common.net.lan;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class ThreadFindLanServers extends Thread {

    private final InetAddress broadcastAddress;

    private final MulticastSocket socket;

    private Map<InetSocketAddress, ServerEntry> foundServers = new HashMap<InetSocketAddress, ServerEntry>();

    public ThreadFindLanServers() throws IOException {
        super("Find Lan Servers Thread");
        this.broadcastAddress = InetAddress.getByName("225.0.2.60");
        this.socket = new MulticastSocket(4446);
        this.socket.setSoTimeout(0);
        this.socket.joinGroup(this.broadcastAddress);
    }

    @Override
    public void run() {
        byte[] bytes = new byte[1024];

        while (!this.isInterrupted()) {
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = new String(packet.getData(), packet.getOffset(), packet.getLength());
            ServerInfo info = new ServerInfo(s);
            InetSocketAddress address = InetSocketAddress.createUnresolved(info.getIp(), info.getPort());
            foundServers.put(address, new ServerEntry(info));
        }
    }

    public Map<InetSocketAddress, ServerEntry> getFoundServers() {
        return foundServers;
    }

    public static class ServerEntry {
        private ServerInfo info;

        public ServerEntry(ServerInfo info) {
            this.info = info;
        }

        public ServerInfo getInfo() {
            return info;
        }
    }
}
