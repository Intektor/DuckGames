package de.intektor.duckgames.common.chat;

import com.badlogic.gdx.graphics.Color;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.game.GameProfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class PlayerChatMessage implements ChatMessage {

    private GameProfile playerProfile;
    private String message;

    public PlayerChatMessage(GameProfile playerProfile, String message) {
        this.playerProfile = playerProfile;
        this.message = message;
    }

    public PlayerChatMessage() {
    }

    public GameProfile getPlayerProfile() {
        return playerProfile;
    }

    @Override
    public Color getMessageColor() {
        return Color.WHITE;
    }

    @Override
    public String getMessage() {
        return String.format("%s: %s", playerProfile.username, message);
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, playerProfile.profileUUID);
        out.writeUTF(message);
    }

    @Override
    public void readFromStream(DataInputStream in) throws IOException {
        playerProfile = CommonCode.proxy.getGameProfiles().get(NetworkUtils.readUUID(in));
        message = in.readUTF();
    }
}
