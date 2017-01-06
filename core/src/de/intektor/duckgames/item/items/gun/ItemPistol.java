package de.intektor.duckgames.item.items.gun;

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
    protected void shoot(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        System.out.println("shoot");
    }
}
