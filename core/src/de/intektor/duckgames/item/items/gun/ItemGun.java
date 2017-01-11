package de.intektor.duckgames.item.items.gun;

import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.EntityPlayer;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.util.TagUtils;
import de.intektor.duckgames.world.World;
import de.intektor.tag.TagCompound;

/**
 * @author Intektor
 */
public abstract class ItemGun extends Item {

    protected final String shotBulletsTagName = "shotBullets";
    protected final String fireModeTagName = "fireMode";
    protected final String usedReservedAmmoTagName = "usedReservedAmmo";
    protected final FireMode defaultFireMode;

    public ItemGun(String unlocalizedName, EntityEquipmentSlot fittingSlot, FireMode defaultFireMode) {
        super(unlocalizedName, 1, fittingSlot);
        this.defaultFireMode = defaultFireMode;
    }

    @Override
    public void onItemPickup(ItemStack stack, EntityPlayer player, World world) {
        super.onItemPickup(stack, player, world);
        setFireMode(stack, player, world, defaultFireMode);
    }

    @Override
    public void onAttackWithItemBegin(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemBegin(stack, player, world, posX, posY);
        shoot(stack, player, world, posX, posY);
    }

    @Override
    public void onAttackingWithItem(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackingWithItem(stack, player, world, posX, posY);
        if (getFireMode(stack, player, world) == FireMode.AUTO) {
            shoot(stack, player, world, posX, posY);
        }
    }

    @Override
    public void onAttackWithItemEnd(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemEnd(stack, player, world, posX, posY);

    }

    protected abstract void shoot(ItemStack stack, EntityPlayer player, World world, float posX, float posY);

    public int getRemainingBullets(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return getFullMagazineSize(stack, player, world) - tag.getInteger(shotBulletsTagName);
    }

    public void setRemainingBullets(ItemStack stack, EntityPlayer player, World world, int remBullets) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(shotBulletsTagName, getFullMagazineSize(stack, player, world) - remBullets);

    }

    public abstract int getFullMagazineSize(ItemStack stack, EntityPlayer player, World world);

    public FireMode getFireMode(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return FireMode.values()[tag.getInteger(fireModeTagName)];
    }

    public void setFireMode(ItemStack stack, EntityPlayer player, World world, FireMode mode) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(fireModeTagName, mode.ordinal());
    }

    public int getRemainingReserveAmmo(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return getMaxReserveAmmo(stack, player, world) - tag.getInteger(usedReservedAmmoTagName);
    }

    public void setRemainingReserveAmmo(ItemStack stack, EntityPlayer player, World world, int remAmmo) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(usedReservedAmmoTagName, getMaxReserveAmmo(stack, player, world) - remAmmo);
    }

    public abstract int getMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world);

    public enum FireMode {
        SEMI,
        AUTO
    }
}
