package de.intektor.duckgames.block;

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
}
