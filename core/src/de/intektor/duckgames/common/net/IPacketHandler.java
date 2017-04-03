package de.intektor.duckgames.common.net;

/**
 * @author Intektor
 */
public interface IPacketHandler<T extends IPacket> {

    void handlePacket(T packet, AbstractSocket socketFrom);
}
