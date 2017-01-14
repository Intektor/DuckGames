package de.intektor.duckgames.world;

import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.net.server_to_client.BasicEntityUpdateInformationPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.PlayerPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.RemoveEntityPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.SpawnEntityPacketToClient;
import de.intektor.duckgames.editor.EntitySpawn;
import de.intektor.duckgames.editor.spawns.PlayerSpawn;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.EntityPlayer;

import java.util.*;

/**
 * @author Intektor
 */
public class WorldServer extends World {

    private DuckGamesServer server;
    private List<EntitySpawn> entitySpawns;

    public WorldServer(Table<Integer, Integer, Block> blockTable, List<EntitySpawn> entitySpawns, int width, int height, DuckGamesServer server) {
        super(blockTable, width, height, false);
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
        server.messageEveryone(new BasicEntityUpdateInformationPacketToClient(entity));
    }

    @Override
    public void spawnEntityInWorld(Entity entity) {
        server.messageEveryone(new SpawnEntityPacketToClient(entity));
        super.spawnEntityInWorld(entity);
    }

    @Override
    public void removeEntity(Entity entity) {
        server.messageEveryone(new RemoveEntityPacketToClient(entity.uuid));
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
            EntityPlayer player = new EntityPlayer(this, playerSpawn.getX(), playerSpawn.getY(), profile);
            profile.player = player;
            spawnEntityInWorld(player);
            SharedGameRegistries.packetHelper.sendPacket(new PlayerPacketToClient(player.uuid), profile.socket);
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
