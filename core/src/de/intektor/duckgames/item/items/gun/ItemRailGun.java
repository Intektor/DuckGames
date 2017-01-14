package de.intektor.duckgames.item.items.gun;

import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.entity.entities.EntityRail;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class ItemRailGun extends ItemGun {

    public ItemRailGun(String unlocalizedName, EntityEquipmentSlot fittingSlot, FireMode defaultFireMode) {
        super(unlocalizedName, fittingSlot, defaultFireMode);
    }

    @Override
    protected void shoot(ItemStack stack, EntityPlayer player, World world, float angle) {
        float mX = (float) Math.cos(angle);
        float mY = (float) Math.sin(angle);
        EntityRail bullet = new EntityRail(world, player.posX + player.getWidth() / 2, player.posY + player.getEyeHeight(), player, mX, mY);
        world.spawnEntityInWorld(bullet);
    }

    @Override
    public int getDefaultFullMagazineSize(ItemStack stack, EntityPlayer player, World world) {
        return 1;
    }

    @Override
    protected int getDefaultMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world) {
        return 0;
    }

    @Override
    public int getDefaultShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world) {
        return 60;
    }

    @Override
    public float getDefaultRecoil(ItemStack stack, EntityPlayer player, World world) {
        return 15;
    }

    @Override
    public float getDefaultInaccuracy(ItemStack stack, EntityPlayer player, World world) {
        return 0.5f;
    }

    @Override
    public float getWidthInWorld(ItemStack stack) {
        return 0;
    }

    @Override
    public float getHeightInWorld(ItemStack stack) {
        return 0;
    }
}
