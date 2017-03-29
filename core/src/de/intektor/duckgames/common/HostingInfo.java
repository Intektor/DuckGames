package de.intektor.duckgames.common;

/**
 * @author Intektor
 */
public class HostingInfo {

    public final DuckGamesServer.HostingType hostingType;
    public final int port;

    public HostingInfo(DuckGamesServer.HostingType hostingType, int port) {
        this.hostingType = hostingType;
        this.port = port;
    }
}
