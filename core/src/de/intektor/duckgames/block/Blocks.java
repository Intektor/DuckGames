package de.intektor.duckgames.block;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.blocks.BlockAir;
import de.intektor.duckgames.block.blocks.BlockIce;
import de.intektor.duckgames.client.rendering.block.AirBlockBlockRenderer;
import de.intektor.duckgames.client.rendering.block.BlockRendererRegistry;
import de.intektor.duckgames.client.rendering.block.DefaultTextureBlockRenderer;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.CommonCode;

/**
 * @author Intektor
 */
public class Blocks {

    public static final Block DIRT;
    public static final Block GRASS;
    public static final Block ICE;
    public static final Block MARBLE_COBBLESTONE;
    public static final Block CLAY_BRICK;
    public static final Block AIR;
    public static final Block WOODEN_PLANKS;
    public static final Block LEAVES;

    static {
        DIRT = new Block("dirt");
        GRASS = new Block("grass");
        ICE = new BlockIce("ice");
        MARBLE_COBBLESTONE = new Block("marble_cobblestone");
        CLAY_BRICK = new Block("clay_brick");
        AIR = new BlockAir("air");
        WOODEN_PLANKS = new Block("wooden_planks");
        LEAVES = new Block("leaves");
    }

    public static void initCommon() {
        GameRegistry registry = CommonCode.gameRegistry;
        registry.registerBlock(DIRT);
        registry.registerBlock(GRASS);
        registry.registerBlock(ICE);
        registry.registerBlock(MARBLE_COBBLESTONE);
        registry.registerBlock(CLAY_BRICK);
        registry.registerBlock(AIR);
        registry.registerBlock(WOODEN_PLANKS);
        registry.registerBlock(LEAVES);
    }

    public static void initClient() {
        BlockRendererRegistry registry = DuckGamesClient.getDuckGames().getBlockRendererRegistry();
        registry.registerRenderer(DIRT, new DefaultTextureBlockRenderer("dirt"));
        registry.registerRenderer(GRASS, new DefaultTextureBlockRenderer("grass"));
        registry.registerRenderer(ICE, new DefaultTextureBlockRenderer("ice"));
        registry.registerRenderer(MARBLE_COBBLESTONE, new DefaultTextureBlockRenderer("marble_cobblestone"));
        registry.registerRenderer(CLAY_BRICK, new DefaultTextureBlockRenderer("clay_bricks"));
        registry.registerRenderer(AIR, new AirBlockBlockRenderer());
        registry.registerRenderer(WOODEN_PLANKS, new DefaultTextureBlockRenderer("wooden_planks"));
        registry.registerRenderer(LEAVES, new DefaultTextureBlockRenderer("leaves"));
    }
}
