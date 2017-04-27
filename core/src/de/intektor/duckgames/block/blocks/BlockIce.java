package de.intektor.duckgames.block.blocks;

import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class BlockIce extends Block {

    public BlockIce(String unlocalizedName) {
        super(unlocalizedName);
    }

    @Override
    public float getMotionStopFactorOnStep(EntityPlayer player, World world) {
        return 0.9f;
    }

    @Override
    public boolean hasAcceleratedMovement(EntityPlayer player, World world) {
        return true;
    }

    @Override
    public float getMaxMotion(EntityPlayer player, World world) {
        return 1.3f;
    }

    @Override
    public float getMotionAccelerationAdditionWhenMoving(EntityPlayer player, World world) {
        return 0.1f;
    }
}
