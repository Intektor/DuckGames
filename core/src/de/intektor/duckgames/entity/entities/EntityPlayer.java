package de.intektor.duckgames.entity.entities;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.server_to_client.PickupEquipmentItemStackPacketToClient;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityDirection;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.util.EnumAxis;
import de.intektor.duckgames.util.EnumDirection;
import de.intektor.duckgames.world.World;
import de.intektor.duckgames.world.WorldServer;

import java.util.List;
import java.util.UUID;

/**
 * @author Intektor
 */
public abstract class EntityPlayer extends Entity {

    private PlayerProfile profile;

    private boolean movingLeft, movingRight;

    public boolean isJumping;
    public int maxJumpTicks = 50;
    public int jumpTicks;

    private boolean isAttacking;
    private float attackPosX, attackPosY;

    public float recoilAngle;
    public long worldTimeAtLastShot;

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
            motionY += 0.275f;
        }
        if (!world.isRemote) {
            List<EntityItem> entitiesInRegion = world.getEntitiesInRegion(EntityItem.class, new Collision2D(posX - 1, posY, 2, 1));
            for (EntityItem entityItem : entitiesInRegion) {
                if (entityItem.canBePickedUpByPlayer(this)) {
                    Item item = entityItem.getItemStack().getItem();
                    if (getEquipment(item.getFittingSlot()) == null) {
                        setEquipment(item.getFittingSlot(), entityItem.getItemStack());
                        world.removeEntity(entityItem);
                        ((WorldServer) world).getServer().broadcast(new PickupEquipmentItemStackPacketToClient(uuid, item.getFittingSlot(), entityItem.getItemStack()));
                        item.onItemPickup(entityItem.getItemStack(), this, world);
                    }
                }
            }
            if (world.getWorldTime() - worldTimeAtLastShot > 10) recoilAngle = 0;
        }
        for (int i = 0; i < equipment.length; i++) {
            ItemStack equip = equipment[i];
            if (equip != null) {
                equip.getItem().itemHeld(equip, this, world, EntityEquipmentSlot.values()[i]);
            }
        }
        if (isAttacking) {
            ItemStack equipment = getEquipment(EntityEquipmentSlot.MAIN_HAND);
            equipment.getItem().onAttackingWithItem(equipment, this, world, attackPosX, attackPosY);
        }
    }

    @Override
    protected void collidedInAxis(EnumAxis axis, float collisionPointX, float collisionPointY, float motionX, float motionY) {
        super.collidedInAxis(axis, collisionPointX, collisionPointY, motionX, motionY);
        if (!world.isRemote) {
            if (axis == EnumAxis.X && onGround) {
                float cX = posX + motionX;
                int cY = (int) posY;
                for (int y = cY; y <= cY + stepHeight; y++) {
                    if (canBeAtPosition(cX, y)) {
                        posX = cX;
                        posY = y;
                        break;
                    }
                }
            }
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
                    setDirection(EntityDirection.LEFT);
                }
                break;
            case RIGHT:
                movingRight = start;
                if (start) {
                    setDirection(EntityDirection.RIGHT);
                }
                break;
        }
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
