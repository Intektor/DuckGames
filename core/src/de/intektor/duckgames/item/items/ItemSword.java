package de.intektor.duckgames.item.items;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityDirection;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.game.damage.DamageSource;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.World;

import java.util.List;

/**
 * @author Intektor
 */
public class ItemSword extends Item {

    public ItemSword() {
        super("sword", 1, EntityEquipmentSlot.MAIN_HAND);
    }

    @Override
    public void onAttackWithItemBegin(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemBegin(stack, player, world, posX, posY);
        if (!world.isRemote) {
            float swordRange = 2.0f;
            player.setDirection(posX > player.posX + player.getWidth() / 2 ? EntityDirection.RIGHT : EntityDirection.LEFT);
            List<Entity> entitiesInRegion = world.getEntitiesInRegion(Entity.class, new Collision2D(player.posX + player.getWidth() / 2, player.posY + player.getEyeHeight(), player.getDirection() == EntityDirection.LEFT ? -swordRange : swordRange, 1));
            entitiesInRegion.remove(player);
            for (Entity entity : entitiesInRegion) {
                entity.damageEntity(new DamageSource(player, 3f));
            }
        }
    }

    @Override
    public void onAttackingWithItem(ItemStack stack, EntityPlayer player, World world, float aimAngle) {
        super.onAttackingWithItem(stack, player, world, aimAngle);
    }

    @Override
    public void onAttackWithItemEnd(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemEnd(stack, player, world, posX, posY);
    }

    @Override
    public void onItemHeld(ItemStack stack, EntityPlayer player, World world) {
        super.onItemHeld(stack, player, world);
    }

    @Override
    public void onItemUse(ItemStack stack, EntityPlayer player, World world) {
        super.onItemUse(stack, player, world);
    }

    @Override
    public void onItemPickup(ItemStack stack, EntityPlayer player, World world) {
        super.onItemPickup(stack, player, world);
    }

    @Override
    public void onItemThrownAway(ItemStack stack, EntityPlayer player, World world) {
        super.onItemThrownAway(stack, player, world);
    }
}
