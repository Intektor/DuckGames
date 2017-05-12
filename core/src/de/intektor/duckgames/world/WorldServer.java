package de.intektor.duckgames.world;

import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.client.editor.EntitySpawn;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.entity.EntityPlayerMP;
import de.intektor.duckgames.common.net.server_to_client.BasicEntityUpdateInformationPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.PlayerPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.RemoveEntityPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.SpawnEntityPacketToClient;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.game.worlds.spawns.PlayerSpawn;

import java.util.*;

/**
 * @author Intektor
 */
public class WorldServer extends World {

    private DuckGamesServer server;
    private List<EntitySpawn> entitySpawns;

    public WorldServer(Table<Integer, Integer, Block> blockTable, List<EntitySpawn> entitySpawns, int width, int height, DuckGamesServer server, DuckGamesServer.GameMode gameMode) {
        super(blockTable, width, height, false, gameMode);
        this.server = server;
        this.entitySpawns = entitySpawns;
    }

    @Override
    public void updateWorld() {
        super.updateWorld();
    }


    @Override
    protected void updateEntity(Entity entity) {
        super.updateEntity(entity);
        server.broadcast(new BasicEntityUpdateInformationPacketToClient(entity));
    }

    @Override
    public void entityKilled(Entity entity) {
        super.entityKilled(entity);
        if (entity instanceof EntityPlayer) {
            int alive = 0;
            EntityPlayer lastAlive = null;
            for (EntityPlayer player : playerList) {
                if (!player.isDead) {
                    alive++;
                    lastAlive = player;
                }
            }
            if (alive == 1) {
                server.getMainServerThread().endRound(this, (EntityPlayerMP) lastAlive);
            }
        }
    }

    @Override
    public void spawnEntityInWorld(Entity entity) {
        server.broadcast(new SpawnEntityPacketToClient(entity));
        super.spawnEntityInWorld(entity);
    }

    @Override
    public void removeEntity(Entity entity) {
        server.broadcast(new RemoveEntityPacketToClient(entity.uuid));
        super.removeEntity(entity);
    }

    /**
     * Spawns all players in world, sends them to the clients, and tells the clients which player they are
     */
    public void spawnPlayers() {
        List<PlayerSpawn> playerSpawnList = new ArrayList<PlayerSpawn>();
        final Random r = new Random();
        for (EntitySpawn entitySpawn : entitySpawns) {
            if (entitySpawn instanceof PlayerSpawn) {
                playerSpawnList.add((PlayerSpawn) entitySpawn);
            }
        }

        Collections.sort(playerSpawnList, new Comparator<PlayerSpawn>() {
            @Override
            public int compare(PlayerSpawn o1, PlayerSpawn o2) {
                return r.nextInt(3) - 1;
            }
        });

        List<PlayerProfile> remainingProfiles = new ArrayList<PlayerProfile>(server.getMainServerThread().getProfileMap().values());

        for (PlayerSpawn playerSpawn : playerSpawnList) {
            if (remainingProfiles.size() == 0) return;
            PlayerProfile profile = remainingProfiles.get(r.nextInt(remainingProfiles.size()));
            EntityPlayer player = new EntityPlayerMP(this, playerSpawn.getX(), playerSpawn.getY(), profile);
            profile.gameProfile.player = player;
            spawnEntityInWorld(player);
            CommonCode.packetHelper.sendPacket(new PlayerPacketToClient(player.uuid), profile.socket);
            remainingProfiles.remove(profile);
        }
    }

    public void spawnEntities() {
        for (EntitySpawn entitySpawn : entitySpawns) {
            if (!(entitySpawn instanceof PlayerSpawn)) {
                entitySpawn.spawnInWorld(this);
            }
        }
    }

    public DuckGamesServer getServer() {
        return server;
    }
}
