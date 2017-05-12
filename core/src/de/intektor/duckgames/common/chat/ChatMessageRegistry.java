package de.intektor.duckgames.common.chat;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author Intektor
 */
public class ChatMessageRegistry {

    private BiMap<Integer, Class<? extends IChatMessage>> registry = HashBiMap.create();

    public void register(Class<? extends IChatMessage> clazz) {
        registry.put(registry.size(), clazz);
    }

    public int getIdentifier(Class<? extends IChatMessage> clazz) {
        return registry.inverse().get(clazz);
    }

    public IChatMessage createMessage(int identifier) throws IllegalAccessException, InstantiationException {
        return registry.get(identifier).newInstance();
    }
}
