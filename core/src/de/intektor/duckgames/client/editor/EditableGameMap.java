package de.intektor.duckgames.client.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.files.Serializable;
import de.intektor.duckgames.game.worlds.spawns.ItemSpawner;
import de.intektor.duckgames.game.worlds.spawns.PlayerSpawn;
import de.intektor.duckgames.world.WorldServer;
import de.intektor.duckgames.tag.TagCompound;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class EditableGameMap implements Serializable {

    private static BiMap<Integer, Class<? extends EntitySpawn>> entitySpawnRegistry = HashBiMap.create();
    private static BiMap<Class<? extends EntitySpawn>, Constructor<? extends EntitySpawn>> constructorBiMap = HashBiMap.create();

    static {
        registerEntitySpawn(PlayerSpawn.class, 0);
        registerEntitySpawn(ItemSpawner.class, 1);
    }

    private static void registerEntitySpawn(Class<? extends EntitySpawn> clazz, int id) {
        try {
            Constructor<? extends EntitySpawn> constructor = clazz.getConstructor(float.class, float.class);
            entitySpawnRegistry.put(id, clazz);
            constructorBiMap.put(clazz, constructor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Table<Integer, Integer, Block> blockTable = HashBasedTable.create();
    private List<EntitySpawn> entitySpawnList = new ArrayList<EntitySpawn>();

    private String saveName;

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

    public void removeEntitySpawn(EntitySpawn spawn) {
        entitySpawnList.remove(spawn);
    }

    public void removeEntitySpawns(List<EntitySpawn> list) {
        entitySpawnList.removeAll(list);
    }

    public boolean canPlaceEntitySpawnAtPosition(EntitySpawn spawn, float posX, float posY) {
        float prevPosX = spawn.x;
        float prevPosY = spawn.y;
        spawn.setX(posX - spawn.getWidth() / 2);
        spawn.setY(posY - spawn.getHeight() / 2);
        if (!isCollisionInWorldBounds(spawn.getCollision())) return false;
        if (doesEntitySpawnCollideWithSpawns(spawn, getEntitySpawnList())) return false;
        if (collisionCollidesWithBlocks(spawn.getCollision())) return false;
        spawn.setX(prevPosX);
        spawn.setY(prevPosY);
        return true;
    }

    public boolean canPlaceEntitySpawnAtPosition(EntitySpawn spawn) {
        return canPlaceEntitySpawnAtPosition(spawn, spawn.getX() + spawn.getWidth() / 2, spawn.getY() + spawn.getHeight() / 2);
    }

    public void saveMapToFile(String mapName) {
        try {
            FileHandle handle = Gdx.files.local("saves/user");
            handle.mkdirs();

            handle = Gdx.files.local(String.format("saves/user/%s", mapName));
            DataOutputStream out = new DataOutputStream(new FileOutputStream(handle.file()));
            TagCompound tag = new TagCompound();
            tag.setString("mN", mapName);
            writeToTag(tag);
            tag.writeToStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToTag(TagCompound tag) {
        GameRegistry gameRegistry = CommonCode.gameRegistry;
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

        TagCompound spawnTag = new TagCompound();
        spawnTag.setInteger("eN", entitySpawnList.size());
        for (int i = 0; i < entitySpawnList.size(); i++) {
            EntitySpawn entitySpawn = entitySpawnList.get(i);
            TagCompound entityTag = new TagCompound();
            entityTag.setInteger("ID", entitySpawnRegistry.inverse().get(entitySpawn.getClass()));
            entityTag.setFloat("x", entitySpawn.x);
            entityTag.setFloat("y", entitySpawn.y);
            entitySpawn.writeToTag(entityTag);
            spawnTag.setTag("e" + i, entityTag);
        }
        tag.setTag("e", spawnTag);
    }

    @Override
    public void readFromTag(TagCompound tag) {
        try {
            width = tag.getInteger("w");
            height = tag.getInteger("h");
            saveName = tag.getString("mN");
            GameRegistry gameRegistry = CommonCode.gameRegistry;
            int amtOfB = width * height;
            TagCompound blocksTag = tag.getTag("b");
            for (int i = 0; i < amtOfB; i++) {
                TagCompound blockTag = blocksTag.getTag("" + i);
                int x = blockTag.getInteger("x");
                int y = blockTag.getInteger("y");
                Block b = gameRegistry.getBlock(blockTag.getByte("t"));
                blockTable.put(x, y, b);
            }
            TagCompound spawnTag = tag.getTag("e");
            int amtOfEntities = spawnTag.getInteger("eN");
            for (int i = 0; i < amtOfEntities; i++) {
                TagCompound entityTag = spawnTag.getTag("e" + i);
                float x = entityTag.getFloat("x");
                float y = entityTag.getFloat("y");
                EntitySpawn spawn = constructorBiMap.get(entitySpawnRegistry.get(entityTag.getInteger("ID"))).newInstance(x, y);
                spawn.readFromTag(entityTag);
                entitySpawnList.add(spawn);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public boolean canBeConvertedToWorld() {
        for (EntitySpawn entitySpawn : entitySpawnList) {
            if (!canPlaceEntitySpawnAtPosition(entitySpawn)) return false;
        }
        int amountOfPlayers = 0;
        for (EntitySpawn entitySpawn : entitySpawnList) {
            if (entitySpawn instanceof PlayerSpawn) amountOfPlayers++;
        }
        return amountOfPlayers >= 1;
    }

    public String getSaveName() {
        return saveName;
    }
}
