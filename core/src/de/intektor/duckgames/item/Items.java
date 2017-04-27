package de.intektor.duckgames.item;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.entity.EntityPlayerSP;
import de.intektor.duckgames.client.rendering.item.DefaultTextureItemRenderer;
import de.intektor.duckgames.client.rendering.item.ItemRendererRegistry;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.item.items.ItemSword;
import de.intektor.duckgames.item.items.gun.ItemPistol;
import de.intektor.duckgames.item.items.gun.ItemRailGun;
import de.intektor.duckgames.item.items.gun.ItemTommyGun;
import de.intektor.duckgames.world.WorldClient;

/**
 * @author Intektor
 */
public class Items {

    public static final ItemSword SWORD;
    public static final ItemPistol PISTOL;
    public static final ItemTommyGun TOMMY_GUN;
    public static final ItemRailGun RAIL_GUN;

    static {
        SWORD = new ItemSword();
        PISTOL = new ItemPistol();
        TOMMY_GUN = new ItemTommyGun();
        RAIL_GUN = new ItemRailGun();
    }

    public static void initCommon() {
        GameRegistry gameRegistry = CommonCode.gameRegistry;
        gameRegistry.registerItem(SWORD);
        gameRegistry.registerItem(PISTOL);
        gameRegistry.registerItem(TOMMY_GUN);
        gameRegistry.registerItem(RAIL_GUN);
    }

    public static void initClient() {
        ItemRendererRegistry registry = DuckGamesClient.getDuckGames().getItemRendererRegistry();
        registry.registerRenderer(SWORD, new DefaultTextureItemRenderer("iron_sword") {
            @Override
            protected float getTextureSizeForPlayerRendering(EntityPlayerSP player, WorldClient world) {
                return 1.5f;
            }
        });
        registry.registerRenderer(PISTOL, new DefaultTextureItemRenderer("pistol") {
            @Override
            protected float getTextureSizeForPlayerRendering(EntityPlayerSP player, WorldClient world) {
                return 1;
            }

            @Override
            protected boolean reverseTexture(EntityPlayerSP player, WorldClient world) {
                return true;
            }
        });
        registry.registerRenderer(TOMMY_GUN, new DefaultTextureItemRenderer("tommygun") {
            @Override
            protected float getTextureSizeForPlayerRendering(EntityPlayerSP player, WorldClient world) {
                return 1;
            }
        });
        registry.registerRenderer(RAIL_GUN, new DefaultTextureItemRenderer("rail_guns") {
            @Override
            protected float getTextureSizeForPlayerRendering(EntityPlayerSP player, WorldClient world) {
                return 1;
            }
        });
    }
}
