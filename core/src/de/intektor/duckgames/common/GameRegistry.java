package de.intektor.duckgames.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.item.Item;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Intektor
 */
public class GameRegistry {

    //-------------------------------------------------Blocks-------------------------------------------------

    private BiMap<Byte, Block> blockRegistry = HashBiMap.create();

    public void registerBlock(Block block) {
        blockRegistry.put((byte) blockRegistry.size(), block);
    }

    public Block getBlock(byte id) {
        return blockRegistry.get(id);
    }

    public Set<Block> getAllRegisteredBlocks() {
        return blockRegistry.values();
    }

    public byte getBlockID(Block block) {
        return blockRegistry.inverse().get(block);
    }


    //-------------------------------------------------Entities-------------------------------------------------

    private BiMap<Class<? extends Entity>, Integer> classRegistry = HashBiMap.create();
    private Map<Integer, Constructor<? extends Entity>> constructorRegistry = new HashMap<Integer, Constructor<? extends Entity>>();

    public void registerEntity(Class<? extends Entity> clazz, int identifier) {
        try {
            classRegistry.put(clazz, identifier);
            constructorRegistry.put(identifier, clazz.getConstructor(UUID.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<? extends Entity> getEntityClass(int identifier) {
        return classRegistry.inverse().get(identifier);
    }

    public int getEntityID(Class<? extends Entity> clazz) {
        return classRegistry.get(clazz);
    }

    public Entity createEntity(int identifier, UUID uuid) {
        try {
            return constructorRegistry.get(identifier).newInstance(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-------------------------------------------------Items-------------------------------------------------

    private BiMap<Byte, Item> itemRegistry = HashBiMap.create();

    public void registerItem(Item item) {
        itemRegistry.put((byte) itemRegistry.size(), item);
    }

    public Item getItem(byte id) {
        return itemRegistry.get(id);
    }

    public Set<Item> getAllRegisteredItems() {
        return itemRegistry.values();
    }

    public byte getItemID(Item item) {
        return itemRegistry.inverse().get(item);
    }
}
