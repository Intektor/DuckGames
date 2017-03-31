package de.intektor.duckgames.common.chat;

import de.intektor.duckgames.game.GameProfile;

/**
 * @author Intektor
 */
public class ChatMessage {

    private final GameProfile playerProfile;
    private final String message;

    public ChatMessage(GameProfile playerProfile, String message) {
        this.playerProfile = playerProfile;
        this.message = message;
    }

    public GameProfile getPlayerProfile() {
        return playerProfile;
    }

    public String getMessage() {
        return message;
    }
}
