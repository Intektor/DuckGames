package de.intektor.duckgames.util;

import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class BlockPos {

    public int x, y;
    public Block block;

    public BlockPos(int x, int y, Block block) {
        this.x = x;
        this.y = y;
        this.block = block;
    }
}
