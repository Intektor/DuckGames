package de.intektor.duckgames.client.rendering.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.item.Item;

/**
 * @author Intektor
 */
public class ItemRendererRegistry {

    private BiMap<Item, IItemRenderer> registry = HashBiMap.create();

    public void registerRenderer(Item item, IItemRenderer renderer) {
        registry.put(item, renderer);
    }

    public IItemRenderer getRenderer(Item item) {
        return registry.get(item);
    }

    public Item getItem(IItemRenderer renderer) {
        return registry.inverse().get(renderer);
    }
}
