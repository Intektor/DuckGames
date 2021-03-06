package de.intektor.duckgames.common;

import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.client.ClientProxy;
import de.intektor.duckgames.common.chat.ChatMessageRegistry;
import de.intektor.duckgames.common.chat.PlayerChatMessage;
import de.intektor.duckgames.common.chat.ServerInfoMessage;
import de.intektor.duckgames.common.net.PacketHelper;
import de.intektor.duckgames.common.net.PacketRegistry;
import de.intektor.duckgames.common.net.Side;
import de.intektor.duckgames.common.net.client_to_server.*;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.*;
import de.intektor.duckgames.item.Items;
import de.intektor.duckgames.world.World;

import java.util.UUID;

/**
 * @author Intektor
 */
public class CommonCode {

    public static final PacketHelper packetHelper;
    public static final PacketRegistry packetRegistry;

    public static Networking networking;

    public static final ChatMessageRegistry chatMessageRegistry;

    public static final GameRegistry gameRegistry;

    public static IProxy proxy;

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
        packetRegistry.registerPacket(CurrentPadControllingPacketToServer.class, CurrentPadControllingPacketToServer.Handler.class, 25, Side.SERVER);
        packetRegistry.registerPacket(GameScorePacketToClient.class, GameScorePacketToClient.Handler.class, 26, Side.CLIENT);
        packetRegistry.registerPacket(RoundEndedPacketToClient.class, RoundEndedPacketToClient.Handler.class, 27, Side.CLIENT);
        packetRegistry.registerPacket(DisconnectPacketToServer.class, DisconnectPacketToServer.Handler.class, 28, Side.SERVER);
        packetRegistry.registerPacket(LobbyChangeMapPacketToServer.class, LobbyChangeMapPacketToServer.Handler.class, 29, Side.SERVER);
        packetRegistry.registerPacket(LobbyChangeMapPacketToClient.class, LobbyChangeMapPacketToClient.Handler.class, 30, Side.CLIENT);
        packetRegistry.registerPacket(RemoveProfilePacketToClient.class, RemoveProfilePacketToClient.Handler.class, 31, Side.CLIENT);
        packetRegistry.registerPacket(CrouchingPacketToServer.class, CrouchingPacketToServer.Handler.class, 32, Side.SERVER);
        packetRegistry.registerPacket(NewRoundPacketToClient.class, NewRoundPacketToClient.Handler.class, 33, Side.CLIENT);
        packetRegistry.registerPacket(ChangeSelectedEquipmentPacketToServer.class, ChangeSelectedEquipmentPacketToServer.Handler.class, 34, Side.SERVER);

        chatMessageRegistry = new ChatMessageRegistry();
        chatMessageRegistry.register(PlayerChatMessage.class);
        chatMessageRegistry.register(ServerInfoMessage.class);

        gameRegistry = new GameRegistry();
        gameRegistry.registerEntity(EntityPlayer.class, 0, new BiFunction<Entity, World, UUID>() {
            @Override
            public Entity apply(World world, UUID uuid) {
                return CommonCode.proxy.createPlayer(world, uuid);
            }
        });
        gameRegistry.registerEntity(EntityItem.class, 1);
        gameRegistry.registerEntity(EntityBullet.class, 2);
        gameRegistry.registerEntity(EntityRail.class, 3);
        gameRegistry.registerEntity(EntityGrapplingHook.class, 4);

        proxy = new ClientProxy();

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

    public static void init() {

    }
}
