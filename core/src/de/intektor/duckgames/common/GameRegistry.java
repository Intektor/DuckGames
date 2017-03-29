package de.intektor.duckgames.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.world.World;

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
    private Map<Integer, BiFunction<? extends Entity, World, UUID>> constructorRegistry = new HashMap<Integer, BiFunction<? extends Entity, World, UUID>>();

    public void registerEntity(Class<? extends Entity> clazz, int identifier) {
        try {
            registerEntity(clazz, identifier, new DefaultEntityCreator(clazz));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public <ENTITY extends Entity> void registerEntity(Class<ENTITY> clazz, int identifier, BiFunction<? extends Entity, World, UUID> function) {
        try {
            classRegistry.put(clazz, identifier);
            constructorRegistry.put(identifier, function);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class DefaultEntityCreator implements BiFunction<Entity, World, UUID> {

        private Constructor<? extends Entity> constructor;

        public DefaultEntityCreator(Class<? extends Entity> clazz) throws NoSuchMethodException {
            constructor = clazz.getConstructor(UUID.class);
        }

        @Override
        public Entity apply(World world, UUID uuid) {
            try {
                return constructor.newInstance(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public Class<? extends Entity> getEntityClass(int identifier) {
        return classRegistry.inverse().get(identifier);
    }

    public int getEntityID(Class<? extends Entity> clazz) {
        if (!classRegistry.containsKey(clazz)) {
            Class<?> superclass = clazz.getSuperclass();
            while (superclass != null) {
                if (classRegistry.containsKey(superclass)) return classRegistry.get(superclass);
                superclass = clazz.getSuperclass();
            }
        } else {
            return classRegistry.get(clazz);
        }
        throw new RuntimeException("Unregistered Entity!");
    }

    public Entity createEntity(int identifier, UUID uuid, World world) {
        try {
            return constructorRegistry.get(identifier).apply(world, uuid);
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
