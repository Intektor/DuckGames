package de.intektor.duckgames.world;

import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.util.BlockPos;
import de.intektor.duckgames.util.tracing.RayTraceResult;

import java.util.*;

/**
 * @author Intektor
 */
public abstract class World {

    protected List<Entity> entityList = new ArrayList<Entity>();
    protected List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
    protected Map<UUID, Entity> uuidToEntityMap = new HashMap<UUID, Entity>();

    private List<Entity> addEntitiesNextUpdate = new ArrayList<Entity>();
    private List<Entity> removeEntitiesNextUpdate = new ArrayList<Entity>();

    private Table<Integer, Integer, Block> blockTable;

    private DuckGamesServer.GameMode gameMode;

    protected long worldTime;

    public final boolean isRemote;

    /**
     * The collisions caused by the blocks in the world
     */
    protected List<Collision2D> worldCollisionList = new ArrayList<Collision2D>();

    protected final int width, height;

    public World(Table<Integer, Integer, Block> blockTable, int width, int height, boolean isRemote, DuckGamesServer.GameMode gameMode) {
        this.blockTable = blockTable;
        this.width = width;
        this.height = height;
        this.isRemote = isRemote;
        this.gameMode = gameMode;
        initWorld();
    }

    private void initWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (blockTable.get(x, y).isSolidBlock()) worldCollisionList.add(new Collision2D(x, y, 1, 1));
            }
        }
    }

    public void updateWorld() {
        worldTime++;
        for (Entity entity : addEntitiesNextUpdate) {
            if (entity instanceof EntityPlayer) {
                playerList.add((EntityPlayer) entity);
            } else {
                entityList.add(entity);
            }
        }
        addEntitiesNextUpdate.clear();

        //noinspection SuspiciousMethodCalls
        playerList.removeAll(removeEntitiesNextUpdate);
        entityList.removeAll(removeEntitiesNextUpdate);
        removeEntitiesNextUpdate.clear();

        for (Entity entity : getCombinedEntityList()) {
            updateEntity(entity);
        }
    }

    protected void updateEntity(Entity entity) {
        entity.update();
    }

    /**
     * @return a list with the normal entities and the players
     */
    public List<Entity> getCombinedEntityList() {
        List<Entity> combined = new ArrayList<Entity>();
        combined.addAll(entityList);
        combined.addAll(playerList);
        return combined;
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public List<EntityPlayer> getPlayerList() {
        return playerList;
    }

    public <ENTITY extends Entity> List<ENTITY> getAllEntitiesOfType(Class<ENTITY> clazz) {
        List<ENTITY> list = new ArrayList<ENTITY>();
        for (Entity entity : getCombinedEntityList()) {
            if (entity.getClass() == clazz) {
                list.add((ENTITY) entity);
            }
        }
        return list;
    }

    public void spawnEntityInWorld(Entity entity) {
        addEntitiesNextUpdate.add(entity);
        uuidToEntityMap.put(entity.uuid, entity);
    }

    public void entityKilled(Entity entity) {

    }

    public void removeEntity(Entity entity) {
        removeEntitiesNextUpdate.add(entity);
    }

    public Block getBlock(int x, int y) {
        return blockTable.get(x, y);
    }

    public void setBlock(int x, int y, Block block) {
        blockTable.put(x, y, block);
    }

    public Entity getEntityByUUID(UUID uuid) {
        return uuidToEntityMap.get(uuid);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Table<Integer, Integer, Block> getBlockTable() {
        return blockTable;
    }

    public List<BlockPos> getBlocksInRegion(int x, int y, int width, int height) {
        List<BlockPos> blocks = new ArrayList<BlockPos>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                blocks.add(new BlockPos(x + i, y + j, getBlock(x + i, y + j)));
            }
        }
        return blocks;
    }

    public List<Collision2D> getWorldCollisionList() {
        return worldCollisionList;
    }

    public <ENTITY extends Entity> List<ENTITY> getEntitiesInRegion(Class<? extends ENTITY> clazz, Collision2D collision) {
        List<ENTITY> list = new ArrayList<ENTITY>();
        for (Entity entity : getCombinedEntityList()) {
            if (clazz.isAssignableFrom(entity.getClass())) {
                if (entity.getHitbox().collidesWith(collision)) list.add((ENTITY) entity);
            }
        }
        return list;
    }

    public long getWorldTime() {
        return worldTime;
    }

    public RayTraceResult rayTraceWorld(float sX, float sY, float angle, float distance, boolean ignoreBlocks, boolean ignoreEntities) {
        return rayTraceWorld(sX, sY, angle, distance, ignoreBlocks, ignoreEntities, Collections.<Entity>emptyList());
    }

    public RayTraceResult rayTraceWorld(float sX, float sY, float angle, float distance, boolean ignoreBlocks, boolean ignoreEntities, List<Entity> ignoreEntity) {
        Vector2 p1 = new Vector2(sX, sY);
        Vector2 p2 = new Vector2((float) (sX + Math.cos(angle) * distance), (float) (sY + Math.sin(angle) * distance));
        List<BlockPos> blocksInRegion = getBlocksInRegion((int) sX, (int) sY, (int) distance, (int) distance);
        for (BlockPos blockPos : blocksInRegion) {

        }
        return null;
    }

    public DuckGamesServer.GameMode getGameMode() {
        return gameMode;
    }
}
