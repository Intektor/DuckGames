package de.intektor.duckgames.world;

import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.entity.Entity;

/**
 * @author Intektor
 */
public class WorldClient extends World {

    public WorldClient(Table<Integer, Integer, Block> blockTable, int width, int height) {
        super(blockTable, width, height);
    }

    @Override
    public void spawnEntityInWorld(Entity entity) {

    }
}
