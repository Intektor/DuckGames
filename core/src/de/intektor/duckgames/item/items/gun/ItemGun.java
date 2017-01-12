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
    protected final String worldTimeAtLastShotTagName = "worldTimeAtLastShot";
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
        preShoot(stack, player, world, posX, posY);
    }

    @Override
    public void onAttackingWithItem(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackingWithItem(stack, player, world, posX, posY);
        if (getFireMode(stack, player, world) == FireMode.AUTO) {
            preShoot(stack, player, world, posX, posY);
        }
    }

    @Override
    public void onAttackWithItemEnd(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemEnd(stack, player, world, posX, posY);

    }

    protected void preShoot(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        if (canShoot(stack, player, world)) {
            setTimeAtLastShot(stack, player, world, world.getWorldTime());
            shoot(stack, player, world, posX, posY);
            player.recoilAngle += getRecoil(stack, player, world);
            player.worldTimeAtLastShot = world.getWorldTime();
        }
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

    public void reload(ItemStack stack, EntityPlayer player, World world) {
        int remainingBullets = getRemainingBullets(stack, player, world);
        int fullMagazineSize = getFullMagazineSize(stack, player, world);
        int remainingReserveAmmo = getRemainingReserveAmmo(stack, player, world);
        if (remainingBullets < fullMagazineSize) {
            int possibleBulletsToLoad = fullMagazineSize - remainingBullets;
            int bulletsFromReserve = 0;
            for (int i = possibleBulletsToLoad; i > 0; i--) {
                if (remainingReserveAmmo - i >= 0) {
                    bulletsFromReserve = i;
                    break;
                }
            }
            setRemainingBullets(stack, player, world, remainingBullets + bulletsFromReserve);
            setRemainingReserveAmmo(stack, player, world, remainingReserveAmmo - bulletsFromReserve);
        }
    }

    public void setTimeAtLastShot(ItemStack stack, EntityPlayer player, World world, long worldTime) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setLong(worldTimeAtLastShotTagName, worldTime);
    }

    public long getTimeAtLastShot(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return tag.getLong(worldTimeAtLastShotTagName);
    }

    public abstract int getShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world);

    public boolean canShoot(ItemStack stack, EntityPlayer player, World world) {
        return world.getWorldTime() - getTimeAtLastShot(stack, player, world) >= getShotCooldownInTicks(stack, player, world);
    }

    /**
     * @return the recoil as a angle, that will be added at the next shot
     */
    public abstract float getRecoil(ItemStack stack, EntityPlayer player, World world);

    public enum FireMode {
        SEMI,
        AUTO
    }
}
