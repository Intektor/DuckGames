package de.intektor.duckgames.common.net;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Intektor
 */
public class HttpUtils {

    public static int getSuitableLanPort() {
        ServerSocket socket = null;
        int port = -1;
        try {
            socket = new ServerSocket(0);
            port = socket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (port == -1) throw new RuntimeException("No port found!");
        return port;
    }
}
