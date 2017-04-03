package de.intektor.duckgames.common.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class PacketHelper {

    private PacketRegistry registry;

    public PacketHelper(PacketRegistry registry) {
        this.registry = registry;
    }

    public void writePacket(IPacket packet, DataOutputStream out) throws IOException {
        out.writeInt(registry.getIdentifierByClass(packet.getClass()));
        packet.write(out);
    }

    public IPacket readPacket(DataInputStream in, Side side) throws IOException, PacketOnWrongSideException, IllegalAccessException, InstantiationException {
        IPacket packet;
        packet = registry.getClassByIdentifier(in.readInt()).newInstance();
        if (registry.getSideForPacket(packet.getClass()) != side) {
            throw new PacketOnWrongSideException();
        }
        packet.read(in);
        return packet;
    }

    public void sendPacket(IPacket packet, AbstractSocket to) {
        try {
            writePacket(packet, new DataOutputStream(to.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
