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
public class RoundEndedPacketToClient implements IPacket {

    public GameProfile winner;

    public RoundEndedPacketToClient(GameProfile winner) {
        this.winner = winner;
    }

    public RoundEndedPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, winner.profileUUID);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        winner = CommonCode.proxy.getGameProfiles().get(NetworkUtils.readUUID(in));
    }

    public static class Handler implements IPacketHandler<RoundEndedPacketToClient> {

        @Override
        public void handlePacket(RoundEndedPacketToClient packet, AbstractSocket socket) {
            CommonCode.proxy.handlePacket(packet, socket);
        }
    }
}
