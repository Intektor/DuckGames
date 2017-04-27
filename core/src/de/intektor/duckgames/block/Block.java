package de.intektor.duckgames.block;

import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class Block {

    private String unlocalizedName;

    public Block(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * @return whether an entity can collide with this block or not
     */
    public boolean isSolidBlock() {
        return true;
    }

    /**
     * Called when a player is standing on a block and does not move.
     * The factor will be multiplied with the current xMotion of the player every tick
     *
     * @return the factor by which the motion x will be multiplied
     */
    public float getMotionStopFactorOnStep(EntityPlayer player, World world) {
        return 0;
    }

    /**
     * @return whether this block does special additional acceleration, like ice
     * where there is some amount of motion added every tick till the max motion is reached.
     */
    public boolean hasAcceleratedMovement(EntityPlayer player, World world) {
        return false;
    }

    /**
     * @return what max max motion on this block is when a player moves on it
     */
    public float getMaxMotion(EntityPlayer player, World world) {
        return 1;
    }

    /**
     * Called when a player moves on a block.
     *
     * @param player
     * @param world
     * @return
     */
    public float getMotionAccelerationAdditionWhenMoving(EntityPlayer player, World world) {
        return 0;
    }


    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }
}
