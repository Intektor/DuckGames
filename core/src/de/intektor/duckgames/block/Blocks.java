package de.intektor.duckgames.block;

import com.badlogic.gdx.graphics.Color;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.renderer.block.AirBlockBlockRenderer;
import de.intektor.duckgames.client.renderer.block.BlockRendererRegistry;
import de.intektor.duckgames.client.renderer.block.DefaultColorBlockRenderer;
import de.intektor.duckgames.client.renderer.block.DefaultTextureBlockRenderer;

/**
 * @author Intektor
 */
public class Blocks {

    public static final Block DIRT;
    public static final Block GRASS;
    public static final Block ICE;
    public static final Block STONE_BRICK;
    public static final Block CLAY_BRICK;
    public static final Block AIR;

    static {
        DIRT = new Block("dirt");
        GRASS = new Block("grass");
        ICE = new Block("ice");
        STONE_BRICK = new Block("stone_brick");
        CLAY_BRICK = new Block("clay_brick");
        AIR = new Block("air");
    }

    public static void initUniversal() {
        BlockRegistry registry = DuckGamesClient.getDuckGames().getBlockRegistry();
        registry.registerBlock(DIRT);
        registry.registerBlock(GRASS);
        registry.registerBlock(ICE);
        registry.registerBlock(STONE_BRICK);
        registry.registerBlock(CLAY_BRICK);
        registry.registerBlock(AIR);
    }

    public static void initClient() {
        BlockRendererRegistry registry = DuckGamesClient.getDuckGames().getBlockRendererRegistry();
        registry.registerRenderer(DIRT, new DefaultTextureBlockRenderer("dirt"));
        registry.registerRenderer(GRASS, new DefaultTextureBlockRenderer("grass"));
        registry.registerRenderer(ICE, new DefaultTextureBlockRenderer("ice"));
        registry.registerRenderer(STONE_BRICK, new DefaultColorBlockRenderer(Color.GRAY));
        registry.registerRenderer(CLAY_BRICK, new DefaultColorBlockRenderer(Color.RED));
        registry.registerRenderer(AIR, new AirBlockBlockRenderer());
    }
}
