package de.intektor.duckgames.client.rendering.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class BlockRendererRegistry {

    private BiMap<Block, IBlockRenderer> registry = HashBiMap.create();

    public void registerRenderer(Block block, IBlockRenderer renderer) {
        registry.put(block, renderer);
    }

    public IBlockRenderer getRenderer(Block block) {
        return registry.get(block);
    }

    public Block getBlock(IBlockRenderer renderer) {
        return registry.inverse().get(renderer);
    }
}
