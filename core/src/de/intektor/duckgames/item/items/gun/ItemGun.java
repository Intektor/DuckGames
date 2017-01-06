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

    protected final String shotBulletTagName = "shotBullets";
    protected final String magazineSizeTagName = "maxMagazineSize";
    protected final String fireModeTagName = "fireMode";
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
        return tag.getInteger(shotBulletTagName);
    }

    public void setRemainingBullets(ItemStack stack, EntityPlayer player, World world, int remBullets) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(shotBulletTagName, getFullMagazineSize(stack, player, world) - remBullets);

    }

    public int getFullMagazineSize(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return tag.getInteger(magazineSizeTagName);
    }

    public void setFullMagazineSize(ItemStack stack, EntityPlayer player, World world, int size) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(magazineSizeTagName, size);
    }

    public FireMode getFireMode(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return FireMode.values()[tag.getInteger(fireModeTagName)];
    }

    public void setFireMode(ItemStack stack, EntityPlayer player, World world, FireMode mode) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(fireModeTagName, mode.ordinal());
    }

    public enum FireMode {
        SEMI,
        AUTO
    }
}
