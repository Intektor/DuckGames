package de.intektor.duckgames.common;

/**
 * @author Intektor
 */
public class HostingInfo {

    public final HostingType hostingType;
    public final int port;

    public HostingInfo(HostingType hostingType, int port) {
        this.hostingType = hostingType;
        this.port = port;
    }
}
