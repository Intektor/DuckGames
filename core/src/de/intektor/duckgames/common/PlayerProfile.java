package de.intektor.duckgames.common;

import de.intektor.duckgames.game.GameProfile;

import java.net.Socket;

/**
 * @author Intektor
 */
public class PlayerProfile {

    public GameProfile gameProfile;
    public Socket socket;

    public PlayerProfile(GameProfile profile, Socket socket) {
        this.gameProfile = profile;
        this.socket = socket;
    }

}
