package de.intektor.duckgames.item;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.rendering.item.DefaultTextureItemRenderer;
import de.intektor.duckgames.client.rendering.item.ItemRendererRegistry;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.item.items.ItemSword;
import de.intektor.duckgames.item.items.gun.ItemPistol;
import de.intektor.duckgames.item.items.gun.ItemTommyGun;

/**
 * @author Intektor
 */
public class Items {

    public static final ItemSword SWORD;
    public static final ItemPistol PISTOL;
    public static final ItemTommyGun TOMMY_GUN;

    static {
        SWORD = new ItemSword();
        PISTOL = new ItemPistol();
        TOMMY_GUN = new ItemTommyGun();
    }

    public static void initCommon() {
        GameRegistry gameRegistry = CommonCode.gameRegistry;
        gameRegistry.registerItem(SWORD);
        gameRegistry.registerItem(PISTOL);
        gameRegistry.registerItem(TOMMY_GUN);
    }

    public static void initClient() {
        ItemRendererRegistry registry = DuckGamesClient.getDuckGames().getItemRendererRegistry();
        registry.registerRenderer(SWORD, new DefaultTextureItemRenderer("iron_sword"));
        registry.registerRenderer(PISTOL, new DefaultTextureItemRenderer("pistol"));
        registry.registerRenderer(TOMMY_GUN, new DefaultTextureItemRenderer("tommygun"));
    }
}
