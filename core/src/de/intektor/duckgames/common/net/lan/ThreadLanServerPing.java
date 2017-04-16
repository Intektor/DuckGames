package de.intektor.duckgames.common.net.lan;

import de.intektor.duckgames.common.server.DuckGamesServer;

import java.io.IOException;
import java.net.*;

/**
 * @author Intektor
 */
public class ThreadLanServerPing extends Thread {

    private boolean isStopping;

    private DatagramSocket socket;

    private DuckGamesServer server;

    public ThreadLanServerPing(DuckGamesServer server) throws SocketException {
        super("Ping Lan Server");
        this.server = server;
        this.socket = new DatagramSocket();
    }

    @Override
    public void run() {
        String s = null;
        try {
            s = new ServerInfo(server).convertToString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] bytes = s.getBytes();

        while (!this.isInterrupted() && !isStopping) {
            try {
                InetAddress address = InetAddress.getByName("225.0.2.60");
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 4446);
                this.socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                sleep(1500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isStopping = true;
    }
}
