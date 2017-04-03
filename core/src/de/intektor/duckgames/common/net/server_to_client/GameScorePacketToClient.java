package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.game.GameScore;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class GameScorePacketToClient implements IPacket {

    public GameScore score;

    public GameScorePacketToClient(GameScore score) {
        this.score = score;
    }

    public GameScorePacketToClient() {
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        score.writeToStream(dataOutputStream);
    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {
        score = new GameScore();
        score.readFromStream(dataInputStream);
    }

    public static class Handler implements IPacketHandler<GameScorePacketToClient> {

        @Override
        public void handlePacket(GameScorePacketToClient packet, AbstractSocket socket) {
            CommonCode.proxy.handlePacket(packet, socket);
        }
    }
}
