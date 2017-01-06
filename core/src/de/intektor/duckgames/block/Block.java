package de.intektor.duckgames.block;

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


    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }
}
