package de.intektor.duckgames.block.blocks;

import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class BlockAir extends Block {

    public BlockAir(String unlocalizedName) {
        super(unlocalizedName);
    }

    @Override
    public boolean isSolidBlock() {
        return false;
    }

    @Override
    public float getMotionStopFactorOnStep(EntityPlayer player, World world) {
        return 1;
    }
}
