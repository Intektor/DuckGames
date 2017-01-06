package de.intektor.duckgames.item;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.rendering.item.DefaultTextureItemRenderer;
import de.intektor.duckgames.client.rendering.item.ItemRendererRegistry;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.item.items.ItemSword;
import de.intektor.duckgames.item.items.gun.ItemPistol;

/**
 * @author Intektor
 */
public class Items {

    public static final ItemSword SWORD;
    public static final ItemPistol PISTOL;

    static {
        SWORD = new ItemSword();
        PISTOL = new ItemPistol();
    }

    public static void initCommon() {
        GameRegistry gameRegistry = SharedGameRegistries.gameRegistry;
        gameRegistry.registerItem(SWORD);
        gameRegistry.registerItem(PISTOL);
    }

    public static void initClient() {
        ItemRendererRegistry registry = DuckGamesClient.getDuckGames().getItemRendererRegistry();
        registry.registerRenderer(SWORD, new DefaultTextureItemRenderer("iron_sword"));
        registry.registerRenderer(PISTOL, new DefaultTextureItemRenderer("pistol"));
    }
}
