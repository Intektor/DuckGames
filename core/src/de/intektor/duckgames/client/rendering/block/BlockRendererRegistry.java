package de.intektor.duckgames.client.rendering.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sun.istack.internal.NotNull;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class BlockRendererRegistry {

    private BiMap<Block, IBlockRenderer> registry = HashBiMap.create();

    public void registerRenderer(Block block, IBlockRenderer renderer) {
        registry.put(block, renderer);
    }

    @NotNull
    public IBlockRenderer getRenderer(Block block) {
        return registry.get(block);
    }

    public Block getBlock(IBlockRenderer renderer) {
        return registry.inverse().get(renderer);
    }
}
