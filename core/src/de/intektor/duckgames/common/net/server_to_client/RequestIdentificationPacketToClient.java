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
public class RequestIdentificationPacketToClient implements IPacket {

    @Override
    public void write(DataOutputStream out) throws IOException {

    }

    @Override
    public void read(DataInputStream in) throws IOException {

    }

    public static class Handler implements IPacketHandler<RequestIdentificationPacketToClient> {

        @Override
        public void handlePacket(RequestIdentificationPacketToClient packet, Socket socketFrom) {
            CommonCode.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}
