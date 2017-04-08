package de.intektor.duckgames.game;

import de.intektor.duckgames.entity.entities.EntityPlayer;

import java.util.UUID;

/**
 * @author Intektor
 */
public class GameProfile {

    public final UUID profileUUID;
    public EntityPlayer player;
    public final String username;
    public boolean isHost;

    public GameProfile(UUID profileUUID, String username, boolean isHost) {
        this.profileUUID = profileUUID;
        this.username = username;
        this.isHost = isHost;
    }
}
