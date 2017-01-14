package de.intektor.duckgames.entity.entities;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityItem extends Entity {

    private ItemStack itemStack;
    /**
     * The players uuid that has thrown this item
     */
    private UUID lastThrower;
    private long tickTimeAtThrowAway;

    public EntityItem(World world, float posX, float posY, ItemStack itemStack) {
        super(world, posX, posY);
        this.itemStack = itemStack;
    }

    public EntityItem(World world, float posX, float posY, ItemStack itemStack, EntityPlayer lastThrower) {
        super(world, posX, posY);
        this.itemStack = itemStack;
        this.lastThrower = lastThrower.uuid;
        tickTimeAtThrowAway = lastThrower.worldObj.getWorldTime();
    }

    public EntityItem(UUID uuid) {
        super(uuid);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
    }

    @Override
    protected void updateEntity() {
        if (collision.getWidth() == 0) collision = new Collision2D(posX, posY, getWidth(), getHeight());
    }

    @Override
    public float getWidth() {
        return itemStack != null ? itemStack.getItem().getWidthInWorld(itemStack) - 0.05f : 0;
    }

    @Override
    public float getHeight() {
        return itemStack != null ? itemStack.getItem().getHeightInWorld(itemStack) - 0.05f : 0;
    }

    @Override
    public float getDefaultHealth() {
        return 1;
    }

    @Override
    public float getEyeHeight() {
        return getHeight() * 0.88f;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    protected void writeAdditionalSpawnData(DataOutputStream out) throws IOException {
        super.writeAdditionalSpawnData(out);
        itemStack.writeItemStackToStream(out);
        out.writeBoolean(lastThrower != null);
        if (lastThrower != null) {
            NetworkUtils.writeUUID(out, lastThrower);
            out.writeLong(tickTimeAtThrowAway);
        }
    }

    @Override
    protected void readAdditionalSpawnData(DataInputStream in) throws IOException {
        super.readAdditionalSpawnData(in);
        itemStack = ItemStack.readItemStackFromSteam(in);
        if (in.readBoolean()) {
            lastThrower = NetworkUtils.readUUID(in);
            tickTimeAtThrowAway = in.readLong();
        }
    }

    public boolean canBePickedUpByPlayer(EntityPlayer player) {
        return !player.uuid.equals(lastThrower) || worldObj.getWorldTime() - tickTimeAtThrowAway >= 64;
    }
}
