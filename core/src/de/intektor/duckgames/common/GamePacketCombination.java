package de.intektor.duckgames.common;

import de.intektor.duckgames.common.net.client_to_server.IdentificationPacketToServer;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.entity.EntityPlayer;
import de.intektor.duckgames.entity.EntityRegistry;
import de.intektor.network.PacketHelper;
import de.intektor.network.PacketRegistry;
import de.intektor.network.Side;

/**
 * @author Intektor
 */
public class GamePacketCombination {

    public static final PacketHelper packetHelper;
    public static final PacketRegistry packetRegistry;

    public static final EntityRegistry entityRegistry;

    static {
        packetRegistry = new PacketRegistry();
        packetHelper = new PacketHelper(packetRegistry);

        packetRegistry.registerPacket(RequestIdentificationPacketToClient.class, RequestIdentificationPacketToClient.Handler.class, 0, Side.CLIENT);
        packetRegistry.registerPacket(IdentificationPacketToServer.class, IdentificationPacketToServer.Handler.class, 1, Side.SERVER);
        packetRegistry.registerPacket(KickClientFromServerPacketToClient.class, KickClientFromServerPacketToClient.Handler.class, 2, Side.CLIENT);
        packetRegistry.registerPacket(WorldPacketToClient.class, WorldPacketToClient.Handler.class, 3, Side.CLIENT);
        packetRegistry.registerPacket(PlaceBlockPacketToClient.class, PlaceBlockPacketToClient.Handler.class, 4, Side.CLIENT);
        packetRegistry.registerPacket(SpawnEntityPacketToClient.class, SpawnEntityPacketToClient.Handler.class, 5, Side.CLIENT);
        packetRegistry.registerPacket(PlayerPacketToClient.class, PlayerPacketToClient.Handler.class, 6, Side.CLIENT);
        packetRegistry.registerPacket(FinishedWorldTransmissionPacketToClient.class, FinishedWorldTransmissionPacketToClient.Handler.class, 7, Side.CLIENT);

        entityRegistry = new EntityRegistry();
        entityRegistry.registerEntity(EntityPlayer.class, 0);
    }

}
