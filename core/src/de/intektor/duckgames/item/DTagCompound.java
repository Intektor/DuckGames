package de.intektor.duckgames.item;

import de.intektor.tag.TagCompound;

/**
 * @author Intektor
 */
public class DTagCompound extends TagCompound {

    private boolean isDirty;

    public DTagCompound(TagCompound tag) {
        if (tag != null) {
            this.tags = tag.getTags();
        }
    }

    public DTagCompound() {
    }

    @Override
    public void setByte(String key, byte value) {
        byte prev = getByte(key);
        super.setByte(key, value);
        if (prev != value) isDirty = true;
    }

    @Override
    public void setShort(String key, short value) {
        short prev = getShort(key);
        super.setShort(key, value);
        if (prev != value) isDirty = true;
    }

    @Override
    public void setInteger(String key, int value) {
        int prev = getInteger(key);
        super.setInteger(key, value);
        if (prev != value) isDirty = true;
    }

    @Override
    public void setFloat(String key, float value) {
        float prev = getFloat(key);
        super.setFloat(key, value);
        if (prev != value) isDirty = true;
    }

    @Override
    public void setLong(String key, long value) {
        long prev = getLong(key);
        super.setLong(key, value);
        if (prev != value) isDirty = true;
    }

    @Override
    public void setDouble(String key, double value) {
        double prev = getDouble(key);
        super.setDouble(key, value);
        if (prev != value) isDirty = true;
    }

    @Override
    public void setString(String key, String value) {
        String prev = getString(key);
        super.setString(key, value);
        if (!prev.equals(value)) isDirty = true;
    }

    @Override
    public void setTag(String key, TagCompound value) {
        TagCompound prev = getTag(key);
        super.setTag(key, value);
        if (prev != value) isDirty = true;
    }

    protected boolean isDirty() {
        boolean wasDirty = isDirty;
        isDirty = false;
        return wasDirty;
    }
}
