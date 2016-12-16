package de.intektor.duckgames.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Set;

/**
 * @author Intektor
 */
public class BlockRegistry {

    private BiMap<Byte, Block> registry = HashBiMap.create();

    public void registerBlock(Block block) {
        registry.put((byte) registry.size(), block);
    }

    public Block getBlock(byte id) {
        return registry.get(id);
    }

    public Set<Block> getAllRegisteredBlocks() {
        return registry.values();
    }

    public byte getID(Block block) {
        return registry.inverse().get(block);
    }

}
