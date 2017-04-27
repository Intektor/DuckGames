package de.intektor.duckgames.world;

import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.Entity;

/**
 * @author Intektor
 */
public class WorldClient extends World {

    public WorldClient(Table<Integer, Integer, Block> blockTable, int width, int height, DuckGamesServer.GameMode gameMode) {
        super(blockTable, width, height, true, gameMode);
    }

    @Override
    public void spawnEntityInWorld(Entity entity) {
        entity.world = this;
        super.spawnEntityInWorld(entity);
    }
}
