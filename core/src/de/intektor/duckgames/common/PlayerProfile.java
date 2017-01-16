package de.intektor.duckgames.common;

import de.intektor.duckgames.entity.entities.EntityPlayer;

import java.net.Socket;
import java.util.UUID;

/**
 * @author Intektor
 */
public class PlayerProfile {

    public String username;
    public EntityPlayer player;
    public Socket socket;
    public final UUID profileUUID;

    public PlayerProfile(String username, Socket socket, UUID profileUUID) {
        this.username = username;
        this.socket = socket;
        this.profileUUID = profileUUID;
    }

    public PlayerProfile(String username, UUID profileUUID) {
        this.username = username;
        this.profileUUID = profileUUID;
    }
}
