package de.intektor.duckgames.common.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class NetworkHelper {

    public static void writeUUID(DataOutputStream out, UUID uuid) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    public static UUID readUUID(DataInputStream in) throws IOException {
        return new UUID(in.readLong(), in.readLong());
    }
}
