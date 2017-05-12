package de.intektor.duckgames.entity.entities;

import com.google.common.base.MoreObjects;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.server_to_client.PickupEquipmentItemStackPacketToClient;
import de.intektor.duckgames.data_storage.box.DataBox;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityDirection;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.util.EnumAxis;
import de.intektor.duckgames.util.EnumDirection;
import de.intektor.duckgames.world.World;
import de.intektor.duckgames.world.WorldServer;

import javax.vecmath.Point2f;
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

    public boolean isCrouching;

    private boolean isAttacking;
    private float aimingAngle;

    public float recoilAngle;
    public long worldTimeAtLastShot;

    private ItemStack[] equipment = new ItemStack[EntityEquipmentSlot.values().length];

    private EntityEquipmentSlot currentSelectedEquipment = EntityEquipmentSlot.MAIN_HAND;

    private String displayName;

    private Collision2D standingHitbox;
    private Collision2D crouchingHitbox;

    private EntityGrapplingHook hook;
    private boolean attractedByHookLastTick;

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
        standingHitbox = new Collision2D(0, 0, getWidth(), getHeight());
        crouchingHitbox = new Collision2D(0, 0, getWidth(), getHeight() / 2);

    }

    @Override
    protected void updateEntity() {
        if (!world.isRemote) {
            maxJumpTicks = 3;
            Block block = MoreObjects.firstNonNull(world.getBlock((int) (posX + getWidth() / 2), (int) posY - 1), Blocks.AIR);
            if (isMoving()) {
                if (movingLeft) {
                    if (!block.hasAcceleratedMovement(this, world)) {
                        motionX = -block.getMaxMotion(this, world);
                    } else {
                        motionX = Math.max(motionX - block.getMotionAccelerationAdditionWhenMoving(this, world), -block.getMaxMotion(this, world));
                    }
                }
                if (movingRight) {
                    if (!block.hasAcceleratedMovement(this, world)) {
                        motionX = block.getMaxMotion(this, world);
                    } else {
                        motionX = Math.min(motionX + block.getMotionAccelerationAdditionWhenMoving(this, world), block.getMaxMotion(this, world));
                    }
                }
            }

            if ((movingLeft && movingRight) || (!movingLeft && !movingRight)) {
                motionX = motionX * block.getMotionStopFactorOnStep(this, world);
            }

            if (onGround) jumpTicks = 0;
            if (isJumping && jumpTicks <= maxJumpTicks) {
                jumpTicks++;
                motionY += 0.75f * (1 - jumpTicks / maxJumpTicks);
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

            if (hook != null && hook.isCollided()) {
                Point2f pMid = getMiddle();
                Point2f hMid = hook.getMiddle();
                float dX = pMid.x - hMid.x;
                float dY = pMid.y - hMid.y;
                if (Math.sqrt(dX * dX + dY * dY) >= hook.getRopeLength() - 1) {
                    float actualAngle = (float) Math.atan2(pMid.y - hMid.y, pMid.x - hMid.x);
                    int i = actualAngle > -Math.PI / 2 ? 1 : -1;
                    if (!attractedByHookLastTick) {
                        hook.setAngle(actualAngle);
                        float rX = (float) (motionX * Math.cos(hook.getAngle()));
                        float rY = (float) (motionY * Math.sin(hook.getAngle()));
                        float totalV = (float) -Math.sqrt(rX * rX + rY * rY) / hook.getRopeLength();
                        hook.setAngleVelocity(hook.getAngleVelocity() + i * totalV);
                    }
                    float mAccel = (float) (i * (((movingLeft ? -1 : movingRight ? 1 : 0) * Math.cos(hook.getAngle()) * 0.05f) / hook.getRopeLength()));
                    hook.setAngleVelocity(hook.getAngleVelocity() + mAccel);
                    float accel = (float) (-getGravitationalVelocity() / hook.getRopeLength() * Math.sin(hook.getAngle() + Math.PI / 2));
                    hook.setAngleVelocity((hook.getAngleVelocity() + accel) * 0.99f);
                    hook.setAngle(hook.getAngle() + hook.getAngleVelocity());

                    motionX = (float) ((hMid.x + Math.cos(hook.getAngle()) * hook.getRopeLength()) - pMid.x);
                    motionY = (float) ((hMid.y + Math.sin(hook.getAngle()) * hook.getRopeLength()) - pMid.y);
                    attractedByHookLastTick = true;
                } else {
                    hook.setAngleVelocity(0);
                    attractedByHookLastTick = false;
                }
            }
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
            if (isMoving()) {
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
    }

    @Override
    public float getWidth() {
        return 1.8f;
    }

    @Override
    public float getHeight() {
        return !isCrouching ? 2.4f : 1.2f;
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

    public ItemStack getCurrentlySelectedEquipment() {
        return getEquipment(currentSelectedEquipment);
    }

    public void setAttacking(Status status) {
        isAttacking = status != Status.END;
    }

    public float getAimingAngle() {
        return aimingAngle;
    }

    public void setAim(float aimingAngle, float aimingStrength) {
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

    public void setJumping(boolean start) {
        isJumping = start && onGround;
        if (!start && onGround) {
            jumpTicks = maxJumpTicks;
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isMoving() {
        return movingLeft || movingRight;
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

    @Override
    public void writeAdditionalUpdateData(DataBox box) {
        super.writeAdditionalUpdateData(box);
        box.writeFloat(aimingAngle);
        box.writeBoolean(isCrouching);
        box.writeInt(currentSelectedEquipment.ordinal());
    }

    @Override
    public void readAdditionalUpdateData(DataBox box) {
        super.readAdditionalUpdateData(box);
        aimingAngle = box.readFloat();
        isCrouching = box.readBoolean();
        currentSelectedEquipment = EntityEquipmentSlot.values()[box.readInt()];
    }

    public void setCurrentSelectedEquipment(EntityEquipmentSlot currentSelectedEquipment) {
        this.currentSelectedEquipment = currentSelectedEquipment;
    }

    public EntityEquipmentSlot getCurrentSelectedEquipment() {
        return currentSelectedEquipment;
    }

    public EntityGrapplingHook getHook() {
        return hook;
    }

    public void setHook(EntityGrapplingHook hook) {
        this.hook = hook;
    }
}
