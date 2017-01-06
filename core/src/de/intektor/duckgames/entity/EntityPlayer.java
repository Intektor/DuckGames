package de.intektor.duckgames.entity;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.server_to_client.PickupEquipmentItemStackPacketToClient;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.util.EnumDirection;
import de.intektor.duckgames.world.World;
import de.intektor.duckgames.world.WorldServer;

import java.util.List;
import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityPlayer extends Entity {

    private PlayerProfile profile;

    private boolean movingLeft, movingRight;

    public boolean isJumping;
    private int maxJumpTicks = 50;
    private int jumpTicks;

    private boolean isAttacking;
    private float attackPosX, attackPosY;

    private ItemStack[] equipment = new ItemStack[EntityEquipmentSlot.values().length];

    public EntityPlayer(World world, float posX, float posY, PlayerProfile profile) {
        super(world, posX, posY);
        this.profile = profile;
    }

    public EntityPlayer(UUID uuid) {
        super(uuid);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        stepHeight = 1.5f;
    }

    @Override
    protected void updateEntity() {
        maxJumpTicks = 10;
        if (movingLeft) motionX = -1;
        if (movingRight) motionX = 1;
        if ((movingLeft && movingRight) || (!movingLeft && !movingRight)) motionX = 0;
        if (onGround) jumpTicks = 0;
        if (isJumping && jumpTicks <= maxJumpTicks) {
            jumpTicks++;
            motionY += 0.4f;
        }
        if (!worldObj.isRemote) {
            List<EntityItem> entitiesInRegion = worldObj.getEntitiesInRegion(EntityItem.class, new Collision2D(posX - 1, posY, 2, 1));
            for (EntityItem entityItem : entitiesInRegion) {
                if (entityItem.canBePickedUpByPlayer(this)) {
                    Item item = entityItem.getItemStack().getItem();
                    if (getEquipment(item.getFittingSlot()) == null) {
                        setEquipment(item.getFittingSlot(), entityItem.getItemStack());
                        worldObj.removeEntity(entityItem);
                        ((WorldServer) worldObj).getServer().messageEveryone(new PickupEquipmentItemStackPacketToClient(uuid, item.getFittingSlot(), entityItem.getItemStack()));
                        item.onItemPickup(entityItem.getItemStack(), this, worldObj);
                    }
                }
            }
        }
        for (int i = 0; i < equipment.length; i++) {
            ItemStack equip = equipment[i];
            if (equip != null) {
                equip.getItem().itemHeld(equip, this, worldObj, EntityEquipmentSlot.values()[i]);
            }
        }
        if (isAttacking) {
            ItemStack equipment = getEquipment(EntityEquipmentSlot.MAIN_HAND);
            equipment.getItem().onAttackingWithItem(equipment, this, worldObj, attackPosX, attackPosY);
        }
    }

    @Override
    public float getWidth() {
        return 1.8f;
    }

    @Override
    public float getHeight() {
        return 2.4f;
    }

    @Override
    public float getDefaultHealth() {
        return 3;
    }

    @Override
    public float getEyeHeight() {
        return getHeight() * 0.88f;
    }

    public void move(EnumDirection direction, boolean start) {
        switch (direction) {
            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                movingLeft = start;
                if (start) {
                    isLeft = true;
                    isRight = false;
                }
                break;
            case RIGHT:
                movingRight = start;
                if (start) {
                    isRight = true;
                    isLeft = false;
                }
                break;
        }
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setEquipment(EntityEquipmentSlot slot, ItemStack stack) {
        equipment[slot.ordinal()] = stack;
    }

    public ItemStack getEquipment(EntityEquipmentSlot slot) {
        return equipment[slot.ordinal()];
    }

    public void setAttacking(Status status, float posX, float posY) {
        isAttacking = status != Status.END;
        attackPosX = posX;
        attackPosY = posY;
    }

    public boolean isAttacking() {
        return isAttacking;
    }
}
