package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;
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

    public PlayerProfile profile;

    public PlayerProfilesPacketToClient(PlayerProfile profile) {
        this.profile = profile;
    }

    public PlayerProfilesPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(profile.username);
        NetworkUtils.writeUUID(out, profile.profileUUID);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        profile = new PlayerProfile(in.readUTF(), NetworkUtils.readUUID(in));
    }

    public static class Handler implements IPacketHandler<PlayerProfilesPacketToClient> {

        @Override
        public void handlePacket(PlayerProfilesPacketToClient packet, Socket socket) {
            CommonCode.clientProxy.handlePacket(packet, socket);
        }
    }
}
