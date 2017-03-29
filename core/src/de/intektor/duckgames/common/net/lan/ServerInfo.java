package de.intektor.duckgames.common.net.lan;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Intektor
 */
public class ServerInfo {

    private String ip;
    private int port;
    private int playingPlayers;
    private DuckGamesServer.ServerState state;

    public ServerInfo(DuckGamesServer server) throws UnknownHostException {
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.port = server.getPort();
        this.playingPlayers = server.getMainServerThread().getProfileMap().size();
        this.state = server.getServerState();
    }

    public ServerInfo(String s) {
        List<String> list = StringUtils.splitConcated("[NI]", s);
        ip = list.get(0);
        port = Integer.parseInt(list.get(1));
        playingPlayers = Integer.parseInt(list.get(2));
        state = DuckGamesServer.ServerState.values()[Integer.parseInt(list.get(3))];
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getPlayingPlayers() {
        return playingPlayers;
    }

    public DuckGamesServer.ServerState getState() {
        return state;
    }

    public String convertToString() {
        return StringUtils.concatWithSplit("[NI]", ip, port, playingPlayers, state.ordinal());
    }
}
