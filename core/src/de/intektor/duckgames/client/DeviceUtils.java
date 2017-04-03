package de.intektor.duckgames.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import java.net.*;
import java.util.Enumeration;

/**
 * @author Intektor
 */
public class DeviceUtils {

    public static boolean isDeviceTouch() {
        Application.ApplicationType type = Gdx.app.getType();
        return type == Application.ApplicationType.Android || type == Application.ApplicationType.iOS;
    }

    public static InetAddress getLanAddress() {
        try {
            for (final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                final NetworkInterface cur = interfaces.nextElement();
                if (cur.isLoopback()) {
                    continue;
                }
                for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                    final InetAddress inet_addr = addr.getAddress();
                    if (!(inet_addr instanceof Inet4Address)) {
                        continue;
                    }
                    return InetAddress.getByName(inet_addr.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
