package de.intektor.duckgames.common.net.lan;

import de.intektor.duckgames.client.DeviceUtils;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.util.StringUtils;

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
    private DuckGamesServer.GameMode gameMode;

    public ServerInfo(DuckGamesServer server) throws UnknownHostException {
        this.ip = DeviceUtils.getLanAddress().getHostAddress();
        this.port = server.getPort();
        this.playingPlayers = server.getMainServerThread().getProfileMap().size();
        this.state = server.getServerState();
        this.gameMode = server.getMainServerThread().getGameMode();
    }

    public ServerInfo(String s) {
        List<String> list = StringUtils.splitConcated("[NI]", s);
        ip = list.get(0);
        port = Integer.parseInt(list.get(1));
        playingPlayers = Integer.parseInt(list.get(2));
        state = DuckGamesServer.ServerState.values()[Integer.parseInt(list.get(3))];
        gameMode = DuckGamesServer.GameMode.values()[Integer.parseInt(list.get(4))];
    }

    public ServerInfo(String ip, int port, int playingPlayers) {
        this.ip = ip;
        this.port = port;
        this.playingPlayers = playingPlayers;
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

    public DuckGamesServer.GameMode getGameMode() {
        return gameMode;
    }

    public String convertToString() {
        return StringUtils.concatWithSplit("[NI]", ip, port, playingPlayers, state.ordinal(), gameMode.ordinal());
    }
}
