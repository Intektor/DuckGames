package de.intektor.duckgames.common.server;

/**
 * @author Intektor
 */
public class ServerStartingInfo {

    public boolean started;
    public Exception upnpException;

    public boolean upnpSuccessful() {
        return upnpException == null;
    }

}
