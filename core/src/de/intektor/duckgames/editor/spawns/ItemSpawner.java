package de.intektor.duckgames.editor.spawns;

import com.badlogic.gdx.math.RandomXS128;
import de.intektor.duckgames.editor.EntitySpawn;
import de.intektor.duckgames.entity.EntityItem;
import de.intektor.duckgames.item.DTagCompound;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.WorldServer;

import java.util.List;
import java.util.Random;

/**
 * @author Intektor
 */
public abstract class ItemSpawner extends EntitySpawn {

    private static Random rng = new RandomXS128();

    protected List<ItemSpawn> spawnList;

    public ItemSpawner(float x, float y, List<ItemSpawn> spawnList) {
        super(x, y);
        this.spawnList = spawnList;
    }

    @Override
    public void spawnInWorld(WorldServer world) {
        super.spawnInWorld(world);
        ItemStack itemStack = generateStack(spawnList.get(rng.nextInt(spawnList.size())));
        EntityItem entity = new EntityItem(world, x, y, itemStack);
        world.spawnEntityInWorld(entity);
    }

    protected ItemStack generateStack(ItemSpawn spawn) {
        int stackSize;
        if (spawn.hasFixedStackSize()) {
            stackSize = spawn.getStackSize();
        } else {
            stackSize = rng.nextInt(spawn.getStackSize()) + 1;
        }
        ItemStack stack = new ItemStack(spawn.getItem(), stackSize);
        stack.setTagCompound(spawn.getFixedTagCompound());
        return stack;
    }

    public static class ItemSpawn {

        private Item item;
        private boolean hasFixedStackSize;
        private int stackSize;
        private DTagCompound fixedTagCompound;

        public ItemSpawn(Item item) {
            this(item, true, 1, null);
        }

        public ItemSpawn(Item item, boolean hasFixedStackSize, int stackSize, DTagCompound fixedTagCompound) {
            this.item = item;
            this.hasFixedStackSize = hasFixedStackSize;
            this.stackSize = stackSize;
            this.fixedTagCompound = fixedTagCompound;
        }

        public Item getItem() {
            return item;
        }

        public int getStackSize() {
            return stackSize;
        }

        public boolean hasFixedStackSize() {
            return hasFixedStackSize;
        }

        public DTagCompound getFixedTagCompound() {
            return fixedTagCompound;
        }
    }
}
