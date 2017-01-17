package de.intektor.duckgames.common;

import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.client.ClientProxy;
import de.intektor.duckgames.common.net.client_to_server.*;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.entity.entities.EntityBullet;
import de.intektor.duckgames.entity.entities.EntityItem;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.entity.entities.EntityRail;
import de.intektor.duckgames.item.Items;
import de.intektor.network.PacketHelper;
import de.intektor.network.PacketRegistry;
import de.intektor.network.Side;

/**
 * @author Intektor
 */
public class CommonCode {

    public static final PacketHelper packetHelper;
    public static final PacketRegistry packetRegistry;

    public static final GameRegistry gameRegistry;

    public static IProxy clientProxy;
    public static IProxy serverProxy;

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
        packetRegistry.registerPacket(IdentificationSuccessfulPacketToClient.class, IdentificationSuccessfulPacketToClient.Handler.class, 8, Side.CLIENT);
        packetRegistry.registerPacket(BasicEntityUpdateInformationPacketToClient.class, BasicEntityUpdateInformationPacketToClient.Handler.class, 9, Side.CLIENT);
        packetRegistry.registerPacket(PlayerMovementPacketToServer.class, PlayerMovementPacketToServer.Handler.class, 10, Side.SERVER);
        packetRegistry.registerPacket(JumpPacketToServer.class, JumpPacketToServer.Handler.class, 11, Side.SERVER);
        packetRegistry.registerPacket(RemoveEntityPacketToClient.class, RemoveEntityPacketToClient.Handler.class, 12, Side.CLIENT);
        packetRegistry.registerPacket(DropEquipmentItemStackPacketToClient.class, DropEquipmentItemStackPacketToClient.Handler.class, 13, Side.CLIENT);
        packetRegistry.registerPacket(PlayerDropItemPacketToServer.class, PlayerDropItemPacketToServer.Handler.class, 14, Side.SERVER);
        packetRegistry.registerPacket(PickupEquipmentItemStackPacketToClient.class, PickupEquipmentItemStackPacketToClient.Handler.class, 15, Side.CLIENT);
        packetRegistry.registerPacket(PlayerAttackWithItemPacketToServer.class, PlayerAttackWithItemPacketToServer.Handler.class, 16, Side.SERVER);
        packetRegistry.registerPacket(PlayerAttackWithItemPacketToClient.class, PlayerAttackWithItemPacketToClient.Handler.class, 17, Side.CLIENT);
        packetRegistry.registerPacket(DamageEntityPacketToClient.class, DamageEntityPacketToClient.Handler.class, 18, Side.CLIENT);
        packetRegistry.registerPacket(UpdateEquipmentPacketToClient.class, UpdateEquipmentPacketToClient.Handler.class, 19, Side.CLIENT);
        packetRegistry.registerPacket(ReloadPacketToServer.class, ReloadPacketToServer.Handler.class, 20, Side.SERVER);
        packetRegistry.registerPacket(ChatMessagePacketToServer.class, ChatMessagePacketToServer.Handler.class, 21, Side.SERVER);
        packetRegistry.registerPacket(ChatMessagePacketToClient.class, ChatMessagePacketToClient.Handler.class, 22, Side.CLIENT);
        packetRegistry.registerPacket(PlayerProfilesPacketToClient.class, PlayerProfilesPacketToClient.Handler.class, 23, Side.CLIENT);
        packetRegistry.registerPacket(PlayerJoinLobbyPacketToClient.class, PlayerJoinLobbyPacketToClient.Handler.class, 24, Side.CLIENT);

        gameRegistry = new GameRegistry();
        gameRegistry.registerEntity(EntityPlayer.class, 0);
        gameRegistry.registerEntity(EntityItem.class, 1);
        gameRegistry.registerEntity(EntityBullet.class, 2);
        gameRegistry.registerEntity(EntityRail.class, 3);

        clientProxy = new ClientProxy();
        serverProxy = new ServerProxy();

        Blocks.initCommon();
        Items.initCommon();
    }

    private static DuckGamesServer server;

    public static DuckGamesServer getDuckGamesServer() {
        return server;
    }

    public static void setDuckGamesServer(DuckGamesServer server) {
        CommonCode.server = server;
    }
}
