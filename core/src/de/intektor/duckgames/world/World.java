package de.intektor.duckgames.world;

import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityPlayer;

import java.util.*;

/**
 * @author Intektor
 */
public abstract class World {

    protected List<Entity> entityList = new ArrayList<Entity>();
    protected List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
    protected Map<UUID, Entity> uuidToEntityMap = new HashMap<UUID, Entity>();

    private Table<Integer, Integer, Block> blockTable;

    protected final int width, height;

    public World(Table<Integer, Integer, Block> blockTable, int width, int height) {
        this.blockTable = blockTable;
        this.width = width;
        this.height = height;
    }

    /**
     * @return a list with the normal entities and the players
     */
    protected List<Entity> getCombinedEntityList() {
        List<Entity> combined = new ArrayList<Entity>();
        combined.addAll(entityList);
        combined.addAll(playerList);
        return combined;
    }

    public void spawnEntityInWorld(Entity entity) {
        uuidToEntityMap.put(entity.uuid, entity);
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
}
