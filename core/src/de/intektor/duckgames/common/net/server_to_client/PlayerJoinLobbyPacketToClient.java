package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Intektor
 */
public class PlayerJoinLobbyPacketToClient implements IPacket {

    public UUID playerProfileUUID;

    public PlayerJoinLobbyPacketToClient(UUID playerProfileUUID) {
        this.playerProfileUUID = playerProfileUUID;
    }

    public PlayerJoinLobbyPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, playerProfileUUID);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerProfileUUID = NetworkUtils.readUUID(in);
    }

    public static class Handler implements IPacketHandler<PlayerJoinLobbyPacketToClient> {

        @Override
        public void handlePacket(PlayerJoinLobbyPacketToClient packet, Socket socket) {
            CommonCode.proxy.handlePacket(packet, socket);
        }
    }
}
