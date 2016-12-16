package de.intektor.duckgames.block;

/**
 * @author Intektor
 */
public class Block {

    private String unlocalizedName;

    public Block(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }
}
