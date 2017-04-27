package de.intektor.duckgames.data_storage.tag;

import de.intektor.duckgames.data_storage.storages.StorageBoolean;

/**
 * @author Intektor
 */
public class TagBoolean extends StorageBoolean<String> {

    public TagBoolean(String key) {
        super(key);
    }
}
