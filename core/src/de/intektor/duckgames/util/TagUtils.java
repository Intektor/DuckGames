package de.intektor.duckgames.util;

import de.intektor.duckgames.item.DTagCompound;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.data_storage.tag.TagCompound;

/**
 * @author Intektor
 */
public class TagUtils {

    public static TagCompound getTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new DTagCompound());
        }
        return stack.getTagCompound();
    }
}
