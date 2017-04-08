package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.game.GameProfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class RemoveProfilePacketToClient implements IPacket {

    public GameProfile profile;

    public RemoveProfilePacketToClient(GameProfile profile) {
        this.profile = profile;
    }

    public RemoveProfilePacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, profile.profileUUID);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        profile = CommonCode.proxy.getGameProfiles().get(NetworkUtils.readUUID(in));
    }

    public static class Handler implements IPacketHandler<RemoveProfilePacketToClient> {

        @Override
        public void handlePacket(RemoveProfilePacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
