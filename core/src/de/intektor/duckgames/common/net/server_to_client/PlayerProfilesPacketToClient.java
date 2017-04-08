package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class PlayerProfilesPacketToClient implements IPacket {

    public GameProfile profile;

    public PlayerProfilesPacketToClient(GameProfile profile) {
        this.profile = profile;
    }

    public PlayerProfilesPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, profile.profileUUID);
        out.writeUTF(profile.username);
        out.writeBoolean(profile.isHost);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        profile = new GameProfile(NetworkUtils.readUUID(in), in.readUTF(), in.readBoolean());
    }

    public static class Handler implements IPacketHandler<PlayerProfilesPacketToClient> {

        @Override
        public void handlePacket(PlayerProfilesPacketToClient packet, AbstractSocket socket) {
            CommonCode.proxy.handlePacket(packet, socket);
        }
    }
}
