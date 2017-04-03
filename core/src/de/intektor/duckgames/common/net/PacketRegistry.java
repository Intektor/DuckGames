package de.intektor.duckgames.common.net;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class PacketRegistry {

    private BiMap<Integer, Class<? extends IPacket>> packetRegistry = HashBiMap.create();
    private BiMap<Class<? extends IPacket>, Class<? extends IPacketHandler>> packetHandlerRegistry = HashBiMap.create();
    private Map<Class<? extends IPacket>, Side> sideMap = new HashMap<Class<? extends IPacket>, Side>();

    /**
     * Registers a packet
     *
     * @param clazz      the packet class
     * @param identifier the identifier
     * @param side       the side where it should be handled
     */
    public void registerPacket(Class<? extends IPacket> clazz, int identifier, Side side) {
        packetRegistry.put(identifier, clazz);
        sideMap.put(clazz, side);
    }

    public <PACKET extends IPacket, HANDLER extends IPacketHandler<PACKET>> void registerPacket(Class<PACKET> packetClass, Class<HANDLER> handlerClass, int identifier, Side side) {
        packetRegistry.put(identifier, packetClass);
        packetHandlerRegistry.put(packetClass, handlerClass);
        sideMap.put(packetClass, side);
    }

    public void registerHandlerForPacket(Class<? extends IPacket> clazz, Class<? extends IPacketHandler> handler) {
        packetHandlerRegistry.put(clazz, handler);
    }

    public Class<? extends IPacketHandler> getHandlerForPacketClass(Class<? extends IPacket> clazz) {
        return packetHandlerRegistry.get(clazz);
    }

    public Side getSideForPacket(Class<? extends IPacket> clazz) {
        return sideMap.get(clazz);
    }

    public int getIdentifierByClass(Class<? extends IPacket> clazz) {
        return packetRegistry.inverse().get(clazz);
    }

    public Class<? extends IPacket> getClassByIdentifier(int identifier) {
        return packetRegistry.get(identifier);
    }
}
