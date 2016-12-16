package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class KickClientFromServerPacketToClient implements IPacket {

    String kickMessage;

    public KickClientFromServerPacketToClient() {
    }

    public KickClientFromServerPacketToClient(String kickMessage) {
        this.kickMessage = kickMessage;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(kickMessage);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        kickMessage = in.readUTF();
    }

    public static class Handler implements IPacketHandler<KickClientFromServerPacketToClient> {

        @Override
        public void handlePacket(KickClientFromServerPacketToClient packet, Socket socketFrom) {
            final DuckGamesClient dg = DuckGamesClient.getDuckGames();
            dg.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    dg.disconnect();
                }
            });
        }
    }
}