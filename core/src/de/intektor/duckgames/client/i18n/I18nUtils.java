package de.intektor.duckgames.client.i18n;

import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.item.Item;

/**
 * @author Intektor
 */
public class I18nUtils {

    public static String getItemName(Item item) {
        return I18n.translate(String.format("item.%s.name", item.getUnlocalizedName()));
    }

    public static String getBlockName(Block block) {
        return I18n.translate(String.format("block.%s.name", block.getUnlocalizedName()));
    }
}
