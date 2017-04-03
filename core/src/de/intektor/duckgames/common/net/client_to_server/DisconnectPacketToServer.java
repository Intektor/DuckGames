package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class DisconnectPacketToServer implements IPacket {
    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {

    }

    public static class Handler implements IPacketHandler<DisconnectPacketToServer> {

        @Override
        public void handlePacket(DisconnectPacketToServer packet, AbstractSocket socket) {

        }
    }
}
