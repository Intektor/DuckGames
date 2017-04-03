package de.intektor.duckgames.common.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public interface IPacket {

    void write(DataOutputStream out) throws IOException;

    void read(DataInputStream in) throws IOException;
}
