package de.intektor.duckgames.common;

import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.game.GameProfile;

/**
 * @author Intektor
 */
public class PlayerProfile {

    public GameProfile gameProfile;
    public AbstractSocket socket;

    public PlayerProfile(GameProfile profile, AbstractSocket socket) {
        this.gameProfile = profile;
        this.socket = socket;
    }

}
