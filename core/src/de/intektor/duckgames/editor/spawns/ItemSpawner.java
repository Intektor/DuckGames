package de.intektor.duckgames.editor.spawns;

import com.badlogic.gdx.math.RandomXS128;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.editor.EntitySpawn;
import de.intektor.duckgames.entity.EntityItem;
import de.intektor.duckgames.item.DTagCompound;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.item.Items;
import de.intektor.duckgames.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Intektor
 */
public class ItemSpawner extends EntitySpawn {

    private static Random rng = new RandomXS128();

    protected List<ItemSpawn> spawnList = new ArrayList<ItemSpawn>();

    public ItemSpawner(float x, float y) {
        super(x, y);
        this.spawnList.add(new ItemSpawn(Items.SWORD));
    }

    @Override
    public float getWidth() {
        return 0.95f;
    }

    @Override
    public float getHeight() {
        return 0.95f;
    }

    @Override
    public Collision2D getCollision() {
        return new Collision2D(x, y, getWidth(), getHeight());
    }

    @Override
    public void spawnInWorld(WorldServer world) {
        super.spawnInWorld(world);
        ItemStack itemStack = spawnList.get(rng.nextInt(spawnList.size())).generateStack();
        EntityItem entity = new EntityItem(world, x, y, itemStack);
        world.spawnEntityInWorld(entity);
    }

    public List<ItemSpawn> getSpawnList() {
        return spawnList;
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

        public ItemStack generateStack() {
            int stackSize;
            if (hasFixedStackSize()) {
                stackSize = getStackSize();
            } else {
                stackSize = rng.nextInt(getStackSize()) + 1;
            }
            ItemStack stack = new ItemStack(getItem(), stackSize);
            stack.setTagCompound(getFixedTagCompound());
            return stack;
        }
    }
}
