package de.intektor.duckgames.item.items.gun;

import de.intektor.duckgames.entity.entities.EntityBullet;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class ItemTommyGun extends ItemGun {

    public ItemTommyGun() {
        super("tommy_gun", EntityEquipmentSlot.MAIN_HAND, FireMode.AUTO);
    }

    @Override
    protected void shoot(ItemStack stack, EntityPlayer player, World world, float angle) {
        if (!world.isRemote) {
            float mX = (float) Math.cos(angle) * 5;
            float mY = (float) Math.sin(angle) * 5;
            EntityBullet bullet = new EntityBullet(world, player.posX + player.getWidth() / 2, player.posY + player.getEyeHeight(), player, mX, mY, 0.5f);
            world.spawnEntityInWorld(bullet);
        }
    }

    @Override
    public int getDefaultFullMagazineSize(ItemStack stack, EntityPlayer player, World world) {
        return 50;
    }

    @Override
    protected int getDefaultMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world) {
        return 50;
    }

    @Override
    public int getDefaultShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world) {
        return 8;
    }

    @Override
    public float getDefaultRecoil(ItemStack stack, EntityPlayer player, World world) {
        return 3;
    }

    @Override
    public float getDefaultInaccuracy(ItemStack stack, EntityPlayer player, World world) {
        return 7.5f;
    }


    @Override
    public float getWidthInWorld(ItemStack stack) {
        return 2;
    }

    @Override
    public float getHeightInWorld(ItemStack stack) {
        return 1;
    }
}
