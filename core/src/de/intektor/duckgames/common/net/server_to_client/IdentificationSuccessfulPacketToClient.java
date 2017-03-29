package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class IdentificationSuccessfulPacketToClient implements IPacket {

    @Override
    public void write(DataOutputStream out) throws IOException {

    }

    @Override
    public void read(DataInputStream in) throws IOException {

    }

    public static class Handler implements IPacketHandler<IdentificationSuccessfulPacketToClient> {

        @Override
        public void handlePacket(IdentificationSuccessfulPacketToClient packet, Socket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
