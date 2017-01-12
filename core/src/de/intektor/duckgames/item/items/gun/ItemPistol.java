package de.intektor.duckgames.item.items.gun;

import de.intektor.duckgames.entity.EntityBullet;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.EntityPlayer;
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
    protected void shoot(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        if (!world.isRemote) {
            int remainingBullets = getRemainingBullets(stack, player, world);
            if (remainingBullets > 0) {
                setRemainingBullets(stack, player, world, remainingBullets - 1);
                float dX = posX - (player.posX + (player.getWidth() / 2));
                float dY = posY - (player.posY + player.getEyeHeight());
                float angle = (float) (Math.atan2(dY, dX) + player.recoilAngle / 180f * Math.PI);
                float motionX = (float) Math.cos(angle) * 5;
                float motionY = (float) Math.sin(angle) * 5;
                EntityBullet bullet = new EntityBullet(world, player.posX + player.getWidth() / 2, player.posY + player.getEyeHeight(), player, motionX, motionY);
                world.spawnEntityInWorld(bullet);
            }
        }
    }

    @Override
    public int getFullMagazineSize(ItemStack stack, EntityPlayer player, World world) {
        return 13;
    }

    @Override
    public int getMaxReserveAmmo(ItemStack stack, EntityPlayer player, World world) {
        return 26;
    }

    @Override
    public int getShotCooldownInTicks(ItemStack stack, EntityPlayer player, World world) {
        return 10;
    }

    @Override
    public float getRecoil(ItemStack stack, EntityPlayer player, World world) {
        return 1;
    }
}
