package de.intektor.duckgames.entity.entities;

import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.util.EnumAxis;
import de.intektor.duckgames.world.World;

import javax.vecmath.Point2f;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityGrapplingHook extends Entity {

    private EntityPlayer shooter;

    private boolean collided;
    private float ropeLength;

    private float angleVelocity, angle;

    private UUID shooterUUID;

    public EntityGrapplingHook(World world, EntityPlayer shooter) {
        super(world, shooter.posX + shooter.getWidth() / 2, shooter.posY + shooter.getEyeHeight());
        this.shooter = shooter;
        motionX = (float) Math.cos(shooter.getAimingAngle()) + shooter.motionX;
        motionY = (float) Math.sin(shooter.getAimingAngle()) + shooter.motionY;
        motionX = (float) Math.cos(shooter.getAimingAngle());
        motionY = (float) Math.sin(shooter.getAimingAngle());
    }

    public EntityGrapplingHook(UUID uuid) {
        super(uuid);
    }

    @Override
    protected void updateEntity() {
        if (shooter == null && shooterUUID != null) {
            shooter = (EntityPlayer) world.getEntityByUUID(shooterUUID);
        }
    }

    @Override
    protected void collidedInAxis(EnumAxis axis, float collisionPointX, float collisionPointY, float motionX, float motionY) {
        super.collidedInAxis(axis, collisionPointX, collisionPointY, motionX, motionY);
        if (!world.isRemote && !collided) {
            collided = true;
            this.motionX = 0;
            this.motionY = 0;
            ropeLength = shooter.getDistanceToEntity(this);
            Point2f sMid = shooter.getMiddle();
            Point2f eMid = getMiddle();
            angle = (float) Math.atan2(sMid.y - eMid.y, sMid.x - eMid.x);
        }
    }

    @Override
    public float getWidth() {
        return 0.1f;
    }

    @Override
    public float getHeight() {
        return 0.1f;
    }

    @Override
    public float getDefaultHealth() {
        return 1;
    }

    @Override
    public float getEyeHeight() {
        return 0;
    }

    @Override
    protected void writeAdditionalSpawnData(DataOutputStream out) throws IOException {
        super.writeAdditionalSpawnData(out);
        NetworkUtils.writeUUID(out, shooter.uuid);
    }

    @Override
    protected void readAdditionalSpawnData(DataInputStream in) throws IOException {
        super.readAdditionalSpawnData(in);
        shooterUUID = NetworkUtils.readUUID(in);
    }

    @Override
    public float getGravitationalVelocity() {
        return 0;
    }

    public EntityPlayer getShooter() {
        return shooter;
    }

    public boolean isCollided() {
        return collided;
    }

    public float getRopeLength() {
        return ropeLength;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngleVelocity() {
        return angleVelocity;
    }

    public void setAngleVelocity(float angleVelocity) {
        this.angleVelocity = angleVelocity;
    }
}
