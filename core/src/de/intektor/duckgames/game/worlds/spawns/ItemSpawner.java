package de.intektor.duckgames.game.worlds.spawns;

import com.badlogic.gdx.math.RandomXS128;
import de.intektor.duckgames.client.editor.EntitySpawn;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.entity.entities.EntityItem;
import de.intektor.duckgames.files.Serializable;
import de.intektor.duckgames.item.DTagCompound;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.item.Items;
import de.intektor.duckgames.world.WorldServer;
import de.intektor.duckgames.data_storage.tag.TagCompound;

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
        this.spawnList.add(new ItemSpawn(Items.GRAPPLE_HOOK));
    }

    @Override
    public float getWidth() {
        return getBiggestWidth() - 0.05f;
    }

    @Override
    public float getHeight() {
        return getBiggestHeight() - 0.05f;
    }

    private ItemSpawn getBiggestItemSpawn() {
        float biggestWidth = 0;
        ItemSpawn biggest = null;
        for (ItemSpawn itemSpawn : spawnList) {
            float widthInWorld = itemSpawn.getItem().getWidthInWorld(itemSpawn.generateStack());
            if (biggestWidth < widthInWorld) {
                biggestWidth = widthInWorld;
                biggest = itemSpawn;
            }
        }
        return biggest;
    }

    private float getBiggestWidth() {
        if (spawnList == null) return 0;
        ItemSpawn biggestItemSpawn = getBiggestItemSpawn();
        return biggestItemSpawn.getItem().getWidthInWorld(biggestItemSpawn.generateStack());
    }

    private ItemSpawn getTallestItemSpawn() {
        float biggestHeight = 0;
        ItemSpawn biggest = null;
        for (ItemSpawn itemSpawn : spawnList) {
            float heightInWorld = itemSpawn.getItem().getHeightInWorld(itemSpawn.generateStack());
            if (biggestHeight < heightInWorld) {
                biggestHeight = heightInWorld;
                biggest = itemSpawn;
            }
        }
        return biggest;
    }

    private float getBiggestHeight() {
        if (spawnList == null) return 0;
        ItemSpawn biggestItemSpawn = getBiggestItemSpawn();
        return biggestItemSpawn.getItem().getHeightInWorld(biggestItemSpawn.generateStack());
    }

    public ItemSpawn getMostImportantItemSpawn() {
        return getWidth() > getHeight() ? getBiggestItemSpawn() : getTallestItemSpawn();
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

    @Override
    public void writeToTag(TagCompound tag) {
        tag.setInteger("iA", spawnList.size());
        for (int i = 0; i < spawnList.size(); i++) {
            ItemSpawn itemSpawn = spawnList.get(i);
            TagCompound itemSpawnTag = new TagCompound();
            itemSpawn.writeToTag(itemSpawnTag);
            tag.setTag("iS" + i, itemSpawnTag);
        }
    }

    @Override
    public void readFromTag(TagCompound tag) {
        int amt = tag.getInteger("iA");
        for (int i = 0; i < amt; i++) {
            TagCompound itemSpawnTag = tag.getTag("iS" + i);
            ItemSpawn spawn = new ItemSpawn();
            spawn.readFromTag(itemSpawnTag);
            spawnList.add(spawn);
        }
    }

    public static class ItemSpawn implements Serializable {

        private Item item;
        private boolean hasFixedStackSize;
        private int stackSize;
        private TagCompound fixedTagCompound;

        public ItemSpawn(Item item) {
            this(item, true, 1, null);
        }

        public ItemSpawn(Item item, boolean hasFixedStackSize, int stackSize, DTagCompound fixedTagCompound) {
            this.item = item;
            this.hasFixedStackSize = hasFixedStackSize;
            this.stackSize = stackSize;
            this.fixedTagCompound = fixedTagCompound;
        }

        public ItemSpawn() {
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

        public TagCompound getFixedTagCompound() {
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
            stack.setTagCompound(new DTagCompound(getFixedTagCompound()));
            return stack;
        }

        @Override
        public void writeToTag(TagCompound tag) {
            tag.setByte("i", CommonCode.gameRegistry.getItemID(item));
            tag.setBoolean("hF", hasFixedStackSize);
            tag.setInteger("ss", stackSize);
            if (fixedTagCompound != null) tag.setTag("fT", fixedTagCompound);
        }

        @Override
        public void readFromTag(TagCompound tag) {
            item = CommonCode.gameRegistry.getItem(tag.getByte("i"));
            hasFixedStackSize = tag.getBoolean("hF");
            stackSize = tag.getInteger("ss");
            fixedTagCompound = tag.getTag("fT");
        }
    }
}
