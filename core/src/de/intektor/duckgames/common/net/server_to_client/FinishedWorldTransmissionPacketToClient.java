package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.gui.guis.GuiPlayState;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class FinishedWorldTransmissionPacketToClient implements IPacket {

    @Override
    public void write(DataOutputStream out) throws IOException {

    }

    @Override
    public void read(DataInputStream in) throws IOException {

    }

    public static class Handler implements IPacketHandler<FinishedWorldTransmissionPacketToClient> {

        @Override
        public void handlePacket(FinishedWorldTransmissionPacketToClient packet, Socket socketFrom) {
            final DuckGamesClient duckGames = DuckGamesClient.getDuckGames();
            duckGames.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    duckGames.showGui(new GuiPlayState());
                }
            });
        }
    }
}