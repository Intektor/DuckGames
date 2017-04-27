package de.intektor.duckgames.files;

import de.intektor.duckgames.data_storage.tag.TagCompound;

/**
 * @author Intektor
 */
public interface Serializable {

    void writeToTag(TagCompound tag);

    void readFromTag(TagCompound tag);
}
