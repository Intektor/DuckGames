package de.intektor.duckgames.entity.entities;

import de.intektor.duckgames.collision.Collision2D;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Intektor
 */
public abstract class EntityPlayer extends Entity {

    private boolean movingLeft, movingRight;

    public boolean isJumping;
    public int maxJumpTicks = 1;
    public int jumpTicks;

    private boolean isAttacking;
    private float aimingAngle;

    public float recoilAngle;
    public long worldTimeAtLastShot;

    private ItemStack[] equipment = new ItemStack[EntityEquipmentSlot.values().length];

    private String displayName;

    public EntityPlayer(World world, float posX, float posY, String displayName) {
        super(world, posX, posY);
        this.displayName = displayName;
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
        if (!world.isRemote) {
            maxJumpTicks = 3;
            if (movingLeft) motionX = -1;
            if (movingRight) motionX = 1;
            if ((movingLeft && movingRight) || (!movingLeft && !movingRight)) motionX = 0;
            if (onGround) jumpTicks = 0;
            if (isJumping && jumpTicks <= maxJumpTicks) {
                jumpTicks++;
                motionY += 0.375f;
            }
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
            if (equipment != null) {
                equipment.getItem().onAttackingWithItem(equipment, this, world, aimingAngle);
            }
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

    public void setAttacking(Status status) {
        isAttacking = status != Status.END;
    }

    public float getAimingAngle() {
        return aimingAngle;
    }

    public void setAim(float aimingAngle, float aimingStrength) {
        float prevAimingAngle = this.aimingAngle;
        this.aimingAngle = aimingAngle;
        direction = aimingAngle > Math.PI / 2 || aimingAngle < -Math.PI / 2 ? EntityDirection.LEFT : EntityDirection.RIGHT;
        if (aimingStrength < 0.5f) {
            if (isAttacking) {
                setAttacking(Status.END);
            }
        } else {
            setAttacking(!isAttacking ? Status.START : Status.UPDATE);
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    protected void writeAdditionalSpawnData(DataOutputStream out) throws IOException {
        super.writeAdditionalSpawnData(out);
        out.writeUTF(displayName);
    }

    @Override
    protected void readAdditionalSpawnData(DataInputStream in) throws IOException {
        super.readAdditionalSpawnData(in);
        displayName = in.readUTF();
    }
}
