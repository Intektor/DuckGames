package de.intektor.duckgames.common.chat;

import de.intektor.duckgames.common.PlayerProfile;

/**
 * @author Intektor
 */
public class ChatMessage {

    private final PlayerProfile playerProfile;
    private final String message;

    public ChatMessage(PlayerProfile playerProfile, String message) {
        this.playerProfile = playerProfile;
        this.message = message;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public String getMessage() {
        return message;
    }
}
