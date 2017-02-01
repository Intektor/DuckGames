package de.intektor.duckgames.item.items.gun;

import de.intektor.duckgames.entity.entities.EntityBullet;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class ItemPistol extends ItemGun {

    public ItemPistol() {
        super("pistol", EntityEquipmentSlot.MAIN_HAND, FireMode.SEMI);
    }

    @Override
    public void onItemHeld(ItemStack stack, EntityPlayer player, World world) {
        super.onItemHeld(stack, player, world);
    }

    @Override
    protected void shoot(ItemStack stack, EntityPlayer player, World world, float angle) {
        float mX = (float) Math.cos(angle) * 5;
        float mY = (float) Math.sin(angle) * 5;
        EntityBullet bullet = new EntityBullet(world, player.posX + player.getWidth() / 2, player.posY + player.getEyeHeight(), player, mX, mY, 1);
        world.spawnEntityInWorld(bullet);
    }

    @Override
    public int getDefaultFullMagazineSize(ItemStack stack, EntityPlayer player, World world) {
        return 13;
    }

    @Override
    protected int getDefaultMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world) {
        return 26;
    }

    @Override
    public int getDefaultShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world) {
        return 10;
    }

    @Override
    public float getDefaultRecoil(ItemStack stack, EntityPlayer player, World world) {
        return 1;
    }

    @Override
    public float getDefaultInaccuracy(ItemStack stack, EntityPlayer player, World world) {
        return 1;
    }

    @Override
    public float getWidthInWorld(ItemStack stack) {
        return 1;
    }

    @Override
    public float getHeightInWorld(ItemStack stack) {
        return 1;
    }
}
