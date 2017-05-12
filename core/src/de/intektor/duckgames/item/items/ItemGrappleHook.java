package de.intektor.duckgames.item.items;

import de.intektor.duckgames.data_storage.tag.TagCompound;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityGrapplingHook;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.util.TagUtils;
import de.intektor.duckgames.world.World;

import java.util.UUID;

/**
 * @author Intektor
 */
public class ItemGrappleHook extends Item {

    private static final String shotGrappleTagName = "shotGrapple";
    private static final String grapplingHookUUIDTagName = "grapplingHookUUID";

    public ItemGrappleHook(String unlocalizedName, int maxStackSize) {
        super(unlocalizedName, maxStackSize, EntityEquipmentSlot.SUPPORT_SLOT);
    }

    @Override
    public void onAttackWithItemBegin(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemBegin(stack, player, world, posX, posY);
        if (!world.isRemote) {
            TagCompound tag = TagUtils.getTag(stack);
            if (!tag.getBoolean(shotGrappleTagName)) {
                EntityGrapplingHook hookEntity = new EntityGrapplingHook(world, player);
                tag.setString(grapplingHookUUIDTagName, hookEntity.uuid.toString());
                tag.setBoolean(shotGrappleTagName, true);
                world.spawnEntityInWorld(hookEntity);
                player.setHook(hookEntity);
            } else {
                UUID uuid = UUID.fromString(tag.getString(grapplingHookUUIDTagName));
                Entity entity = world.getEntityByUUID(uuid);
                entity.kill();
                world.removeEntity(entity);
                tag.setBoolean(shotGrappleTagName, false);
                tag.setString(grapplingHookUUIDTagName, "");
                player.setHook(null);
            }
        }
    }

    @Override
    public void onAttackWithItemEnd(ItemStack stack, EntityPlayer player, World world, float posX, float posY) {
        super.onAttackWithItemEnd(stack, player, world, posX, posY);
    }
}
