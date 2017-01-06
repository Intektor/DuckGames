package de.intektor.duckgames.editor;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.files.Serializable;
import de.intektor.duckgames.world.WorldServer;
import de.intektor.tag.TagCompound;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class EditableGameMap implements Serializable {

    private Table<Integer, Integer, Block> blockTable = HashBasedTable.create();
    private List<EntitySpawn> entitySpawnList = new ArrayList<EntitySpawn>();

    private int width;
    private int height;

    private EditableGameMap() {
    }

    public EditableGameMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Block getBlock(int x, int y) {
        return blockTable.get(x, y);
    }

    public void setBlock(int x, int y, Block block) {
        blockTable.put(x, y, block);
    }

    public Table<Integer, Integer, Block> getBlockTable() {
        return blockTable;
    }

    public void addEntitySpawn(EntitySpawn spawn) {
        entitySpawnList.add(spawn);
    }

    public List<EntitySpawn> getEntitySpawnList() {
        return entitySpawnList;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isCollisionInWorldBounds(Collision2D rect) {
        return rect.x >= 0 && rect.x + rect.getWidth() <= width + 1 && rect.y >= 0 && rect.y + rect.getHeight() <= height + 1;
    }

    public boolean doesEntitySpawnCollideWithSpawns(EntitySpawn spawn, List<EntitySpawn> spawnList) {
        List<EntitySpawn> copy = new ArrayList<EntitySpawn>(spawnList);
        copy.remove(spawn);
        for (EntitySpawn entitySpawn : copy) {
            if (entitySpawn.getCollision().collidesWith(spawn.getCollision())) return true;
        }
        return false;
    }

    public boolean collisionCollidesWithBlocks(Collision2D collision) {
        for (Table.Cell<Integer, Integer, Block> cell : blockTable.cellSet()) {
            if (cell.getValue() != null && cell.getValue().isSolidBlock()) {
                @SuppressWarnings("ConstantConditions") Collision2D bC = new Collision2D(cell.getRowKey(), cell.getColumnKey(), 1, 1);
                if (bC.collidesWith(collision))
                    return true;
            }
        }
        return false;
    }

    public boolean collisionCollidesWithEntitySpawns(Collision2D collision) {
        for (EntitySpawn entitySpawn : entitySpawnList) {
            if (entitySpawn.getCollision().collidesWith(collision)) return true;
        }
        return false;
    }

    public List<EntitySpawn> getEntitySpawnsInRegion(Collision2D collision) {
        List<EntitySpawn> list = new ArrayList<EntitySpawn>();
        for (EntitySpawn entitySpawn : entitySpawnList) {
            if (entitySpawn.getCollision().collidesWith(collision)) list.add(entitySpawn);
        }
        return list;
    }

    public void removeEntitySpawns(List<EntitySpawn> list) {
        entitySpawnList.removeAll(list);
    }

    @Override
    public void writeToTag(TagCompound tag) {
        GameRegistry gameRegistry = SharedGameRegistries.gameRegistry;
        tag.setInteger("w", width);
        tag.setInteger("h", height);
        TagCompound blocksTag = new TagCompound();
        int blockNumber = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Block block = getBlock(x, y);
                TagCompound blockTag = new TagCompound();
                blockTag.setByte("t", gameRegistry.getBlockID(block));
                blockTag.setInteger("x", x);
                blockTag.setInteger("y", y);
                blocksTag.setTag("" + blockNumber++, blockTag);
            }
        }
        tag.setTag("b", blocksTag);
    }

    @Override
    public void readFromTag(TagCompound tag) {
        width = tag.getInteger("w");
        height = tag.getInteger("h");
        GameRegistry gameRegistry = SharedGameRegistries.gameRegistry;
        int amtOfB = width * height;
        TagCompound blocksTag = tag.getTag("b");
        for (int i = 0; i < amtOfB; i++) {
            TagCompound blockTag = blocksTag.getTag("" + i);
            int x = blockTag.getInteger("x");
            int y = blockTag.getInteger("y");
            Block b = gameRegistry.getBlock(blockTag.getByte("t"));
            blockTable.put(x, y, b);
        }
    }

    public static EditableGameMap readMapFromCompound(TagCompound tag) {
        EditableGameMap map = new EditableGameMap();
        map.readFromTag(tag);
        return map;
    }

    public WorldServer convertToWorld(DuckGamesServer server) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (blockTable.get(x, y) == null) blockTable.put(x, y, Blocks.AIR);
            }
        }
        return new WorldServer(blockTable, entitySpawnList, width, height, server);
    }
}
