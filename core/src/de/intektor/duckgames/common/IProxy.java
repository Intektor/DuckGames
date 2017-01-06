package de.intektor.duckgames.common;

import de.intektor.network.IPacket;

import java.net.Socket;

/**
 * @author Intektor
 */
public interface IProxy {

    void handlePacket(IPacket packet, Socket socketFrom);
}
