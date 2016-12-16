package de.intektor.duckgames.common;

import de.intektor.duckgames.entity.EntityPlayer;

import java.net.Socket;

/**
 * @author Intektor
 */
public class PlayerProfile {

    public String username;
    public EntityPlayer player;
    public Socket socket;

    public PlayerProfile(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }
}
