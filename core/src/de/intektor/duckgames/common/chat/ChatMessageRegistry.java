package de.intektor.duckgames.common.chat;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author Intektor
 */
public class ChatMessageRegistry {

    private BiMap<Integer, Class<? extends ChatMessage>> registry = HashBiMap.create();

    public void register(Class<? extends ChatMessage> clazz) {
        registry.put(registry.size(), clazz);
    }

    public int getIdentifier(Class<? extends ChatMessage> clazz) {
        return registry.inverse().get(clazz);
    }

    public ChatMessage createMessage(int identifier) throws IllegalAccessException, InstantiationException {
        return registry.get(identifier).newInstance();
    }
}
