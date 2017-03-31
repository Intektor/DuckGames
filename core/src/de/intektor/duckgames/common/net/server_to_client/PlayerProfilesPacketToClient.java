package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        profile = new GameProfile(NetworkUtils.readUUID(in), in.readUTF());
    }

    public static class Handler implements IPacketHandler<PlayerProfilesPacketToClient> {

        @Override
        public void handlePacket(PlayerProfilesPacketToClient packet, Socket socket) {
            CommonCode.proxy.handlePacket(packet, socket);
        }
    }
}
