package de.intektor.duckgames.item;

import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.SharedGameRegistries;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class ItemStack {

    private Item item;
    private int stackSize;
    private DTagCompound tagCompound;

    public ItemStack(Item item, int stackSize) {
        this.item = item;
        this.stackSize = stackSize;
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    public Item getItem() {
        return item;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public void setTagCompound(DTagCompound tagCompound) {
        this.tagCompound = tagCompound;
    }

    public DTagCompound getTagCompound() {
        return tagCompound;
    }

    public boolean hasTagCompound() {
        return tagCompound != null;
    }

    public void writeItemStackToStream(DataOutputStream out) throws IOException {
        GameRegistry gameRegistry = SharedGameRegistries.gameRegistry;
        out.writeByte(gameRegistry.getItemID(item));
        out.writeInt(stackSize);
        out.writeBoolean(tagCompound != null);
        if (tagCompound != null) tagCompound.writeToStream(out);
    }

    public static ItemStack readItemStackFromSteam(DataInputStream in) throws IOException {
        GameRegistry gameRegistry = SharedGameRegistries.gameRegistry;
        Item item = gameRegistry.getItem(in.readByte());
        int stackSize = in.readInt();
        DTagCompound tagCompound = null;
        if (in.readBoolean()) {
            tagCompound = new DTagCompound();
            tagCompound.readFromStream(in);
        }
        ItemStack itemStack = new ItemStack(item, stackSize);
        itemStack.setTagCompound(tagCompound);
        return itemStack;
    }
}
