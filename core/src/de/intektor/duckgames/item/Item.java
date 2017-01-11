package de.intektor.duckgames.item;

import de.intektor.duckgames.common.net.server_to_client.UpdateEquipmentPacketToClient;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.EntityPlayer;
import de.intektor.duckgames.world.World;
import de.intektor.duckgames.world.WorldServer;

/**
 * @author Intektor
 */
public abstract class Item {

    private String unlocalizedName;
    private final int maxStackSize;
    private EntityEquipmentSlot fittingSlot;

    public Item(String unlocalizedName, int maxStackSize, EntityEquipmentSlot fittingSlot) {
        this.unlocalizedName = unlocalizedName;
        this.maxStackSize = maxStackSize;
        this.fittingSlot = fittingSlot;
    }


    /**
     * Called by {@link EntityPlayer#updateEntity()}
     */
    public final void itemHeld(ItemStack stack, EntityPlayer player, World world, EntityEquipmentSlot slotIn) {
        onItemHeld(stack, player, world);
        if (!world.isRemote) {
            if (stack.hasTagCompound() && stack.getTagCompound().isDirty()) {
                WorldServer serverWorld = (WorldServer) world;
                serverWorld.getServer().messageEveryone(new UpdateEquipmentPacketToClient(player.uuid, stack, slotIn));
            }
        }
    }

    /**
     * Called when a player currently has this item in his inventory
     */
    public void onItemHeld(ItemStack stack, EntityPlayer player, World world) {

    }

    /**
     * Called when the player rightClicks an Item
     */
    public void onItemUse(ItemStack stack, EntityPlayer player, World world) {

    }

    /**
     * Called when a player starts attacking with this item
     */
    public void onAttackWithItemBegin(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {

    }

    /**
     * Called while a player is attacking with this item
     */
    public void onAttackingWithItem(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {

    }

    /**
     * Called when a player ends attacking with this item
     */
    public void onAttackWithItemEnd(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {

    }

    /**
     * Called when a player picks a this item from the ground
     */
    public void onItemPickup(ItemStack stack, EntityPlayer player, World world) {

    }

    /**
     * Called when a player drops a this item to the ground
     */
    public void onItemThrownAway(ItemStack stack, EntityPlayer player, World world) {

    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public EntityEquipmentSlot getFittingSlot() {
        return fittingSlot;
    }
}
