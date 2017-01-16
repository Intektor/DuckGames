package de.intektor.duckgames.item.items.gun;

import com.badlogic.gdx.math.RandomXS128;
import de.intektor.duckgames.entity.EntityDirection;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.util.TagUtils;
import de.intektor.duckgames.world.World;
import de.intektor.tag.TagCompound;

import java.util.Random;

/**
 * @author Intektor
 */
public abstract class ItemGun extends Item {

    protected final String shotBulletsTagName = "shotBullets";
    protected final String fireModeTagName = "fireMode";
    protected final String usedReservedAmmoTagName = "usedReservedAmmo";
    protected final String worldTimeAtLastShotTagName = "worldTimeAtLastShot";
    protected final String fullMagSizeTagName = "magazineSize";
    protected final String reserveAmmoTagName = "reserveAmmunition";
    protected final String recoilAmountTagName = "recoilValue";
    protected final String inaccuracyAmountTagName = "inaccuracyValue";
    protected final String shotCooldownTagName = "cooldownTime";

    protected final FireMode defaultFireMode;

    private Random rng = new RandomXS128();

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
        prepareShoot(stack, player, world, posX, posY);
    }

    @Override
    public void onAttackingWithItem(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackingWithItem(stack, player, world, posX, posY);
        if (getFireMode(stack, player, world) == FireMode.AUTO) {
            prepareShoot(stack, player, world, posX, posY);
        }
    }

    @Override
    public void onAttackWithItemEnd(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemEnd(stack, player, world, posX, posY);

    }

    protected void prepareShoot(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        int remainingBullets = getRemainingBullets(stack, player, world);
        if (canShoot(stack, player, world) && remainingBullets > 0 && !world.isRemote) {
            setTimeAtLastShot(stack, player, world, world.getWorldTime());

            player.setDirection(posX > player.posX + player.getWidth() / 2 ? EntityDirection.RIGHT : EntityDirection.LEFT);

            setRemainingBullets(stack, player, world, remainingBullets - 1);

            float dX = posX - (player.posX + (player.getWidth() / 2));
            float dY = posY - (player.posY + player.getEyeHeight());

            float angle = (float) (Math.atan2(dY, dX) + (player.getDirection() == EntityDirection.LEFT ? -1 : 1) * (player.recoilAngle / 180f * Math.PI));

            if (player.getDirection() == EntityDirection.RIGHT) {
                angle = (float) Math.min(angle, Math.PI / 2);
            } else {
                angle = (float) Math.max(angle, Math.PI * -1.5f);
            }

            float inaccuracy = (float) Math.toRadians(getInaccuracy(stack, player, world));
            inaccuracy *= rng.nextFloat() * 2 - 1;

            angle += inaccuracy;

            shoot(stack, player, world, angle);

            player.recoilAngle += getRecoil(stack, player, world);
            player.recoilAngle = Math.min(player.recoilAngle, 30);
            player.worldTimeAtLastShot = world.getWorldTime();
        }
    }

    protected abstract void shoot(ItemStack stack, EntityPlayer player, World world, float angle);

    public int getRemainingBullets(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        return getFullMagazineSize(stack, player, world) - tag.getInteger(shotBulletsTagName);
    }

    public void setRemainingBullets(ItemStack stack, EntityPlayer player, World world, int remBullets) {
        TagCompound tag = TagUtils.getTag(stack);
        tag.setInteger(shotBulletsTagName, getFullMagazineSize(stack, player, world) - remBullets);

    }

    public int getFullMagazineSize(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        if (tag.getBase(fullMagSizeTagName) != null) {
            return tag.getInteger(fullMagSizeTagName);
        }
        return getDefaultFullMagazineSize(stack, player, world);
    }

    public abstract int getDefaultFullMagazineSize(ItemStack stack, EntityPlayer player, World world);

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

    public int getMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        if (tag.getBase(reserveAmmoTagName) != null) {
            return tag.getInteger(reserveAmmoTagName);
        }
        return getDefaultMaxReserveAmmo(stack, player, world);
    }

    protected abstract int getDefaultMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world);

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

    public int getShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        if (tag.getBase(shotCooldownTagName) != null) {
            return tag.getInteger(shotCooldownTagName);
        }
        return getDefaultShotCooldownInTicks(stack, player, world);
    }

    public abstract int getDefaultShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world);

    public boolean canShoot(ItemStack stack, EntityPlayer player, World world) {
        return world.getWorldTime() - getTimeAtLastShot(stack, player, world) >= getShotCooldownInTicks(stack, player, world);
    }

    /**
     * @return the recoil as a angle, that will be added at the next shot
     */
    public float getRecoil(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        if (tag.getBase(recoilAmountTagName) != null) {
            return tag.getFloat(recoilAmountTagName);
        }
        return getDefaultRecoil(stack, player, world);
    }

    public abstract float getDefaultRecoil(ItemStack stack, EntityPlayer player, World world);

    public float getInaccuracy(ItemStack stack, EntityPlayer player, World world) {
        TagCompound tag = TagUtils.getTag(stack);
        if (tag.getBase(inaccuracyAmountTagName) != null) {
            return tag.getFloat(inaccuracyAmountTagName);
        }
        return getDefaultInaccuracy(stack, player, world);
    }

    public abstract float getDefaultInaccuracy(ItemStack stack, EntityPlayer player, World world);

    @Override
    public abstract float getWidthInWorld(ItemStack stack);

    @Override
    public abstract float getHeightInWorld(ItemStack stack);

    public enum FireMode {
        SEMI,
        AUTO
    }
}
