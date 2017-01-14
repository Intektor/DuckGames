package de.intektor.duckgames.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.DeviceUtils;
import de.intektor.duckgames.client.gui.components.*;
import de.intektor.duckgames.client.gui.components.GuiScrollTool.ScrollToolEntry;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.i18n.I18nUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.client.rendering.item.IItemRenderer;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.editor.spawns.ItemSpawner;
import de.intektor.duckgames.editor.spawns.ItemSpawner.ItemSpawn;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class ItemSpawnEditorGuiComponent extends GuiFrame implements GuiScrollTool.ScrollToolCallback {

    private ItemSpawner itemSpawner;

    private GuiTextBasedButton addSpawnButton;
    private GuiScrollTool<ItemScrollToolEntry> itemSelectionScrollTool;
    private GuiScrollBar scrollBar;

    private BiMap<ItemSpawn, GuiButton> removeButtonMap = HashBiMap.create();

    private final int entrySize = 50;

    private boolean itemSelectionScrollToolLeft;

    public ItemSpawnEditorGuiComponent(int x, int y, int width, int height) {
        super(x, y, width, height, "Edit Item Spawn");

        addSpawnButton = new AddGuiTextBasedButton();
        registerGuiComponent(addSpawnButton);

        itemSelectionScrollTool = new GuiScrollTool<ItemScrollToolEntry>(0, 0, width, height, true, false, DeviceUtils.isDeviceTouch(), width / 5, width / 5, 5, this);
        itemSelectionScrollTool.setShown(false);

        for (Item item : SharedGameRegistries.gameRegistry.getAllRegisteredItems()) {
            itemSelectionScrollTool.addEntry(new ItemScrollToolEntry(item));
        }
        registerGuiComponent(itemSelectionScrollTool);

        scrollBar = new GuiScrollBar(x + width - 20, 0, 20, height, GuiScrollBar.Direction.VERTICAL, 0, height);
        registerGuiComponent(scrollBar);
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
        if (!removeButtonMap.isEmpty()) {
            List<GuiButton> list = new ArrayList<GuiButton>(removeButtonMap.values());
            list.get(0).setEnabled(removeButtonMap.size() > 1);
        }

        if (itemSpawner == null) return;
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == addSpawnButton) {
            moveItemSelectionScrollTool();
            itemSelectionScrollTool.setShown(true);
        } else {
            GuiButton b = null;
            ItemSpawn itemSpawn = null;
            for (GuiButton guiButton : removeButtonMap.values()) {
                if (button == guiButton) {
                    b = guiButton;
                    itemSpawn = removeButtonMap.inverse().get(guiButton);
                    break;
                }
            }
            if (b != null) {
                removeGuiComponent(b);
                removeButtonMap.remove(itemSpawn);
                itemSpawner.getSpawnList().remove(itemSpawn);

            }
        }
    }

    @Override
    protected void drawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {
        sR.begin();
        sR.set(ShapeType.Filled);
        sR.setColor(Color.GRAY);
        sR.rect(drawX, drawY, width, height - topBarHeight);
        sR.end();
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(drawX), GuiUtils.unscaleScreenCoordY(drawY), GuiUtils.unscaleScreenCoordX(width), GuiUtils.unscaleScreenCoordY(height - topBarHeight));
        int offsetY = (int) (scrollBar.getScrollPercent() * (scrollBar.getAllWindowSize() - height + topBarHeight + 50 * 2));
        addSpawnButton.setPosition(0, height - topBarHeight - itemSpawner.getSpawnList().size() * entrySize - entrySize + offsetY);
        float dY = drawY + height - topBarHeight - entrySize + offsetY;
        for (ItemSpawn itemSpawn : itemSpawner.getSpawnList()) {
            renderItemSpawn(itemSpawn, drawX, dY, sR, sB, camera, partialTicks);
            dY -= entrySize;
        }
        int i = 0;
        for (GuiButton guiButton : removeButtonMap.values()) {
            guiButton.setPosition(width - entrySize - 20, height - topBarHeight - i * entrySize - offsetY - entrySize);
            i++;
        }
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public void setItemSpawner(ItemSpawner itemSpawner) {
        this.itemSpawner = itemSpawner;
        itemSelectionScrollTool.setShown(false);
        scrollBar.setAllWindowSize(50);
        componentList.removeAll(removeButtonMap.values());
        for (ItemSpawn itemSpawn : itemSpawner.getSpawnList()) {
            guiHandleNewSpawn(itemSpawn);
        }
        readjustButtonPosition();
    }

    @Override
    public void scrollToolCallback(GuiScrollTool tool, ScrollToolEntry entry) {
        if (tool.isShown()) {
            if (tool == itemSelectionScrollTool) {
                addSpawn(((ItemScrollToolEntry) entry).item);
                tool.setShown(false);
            }
        }
    }

    private void addSpawn(Item item) {
        ItemSpawn e = new ItemSpawn(item);
        itemSpawner.getSpawnList().add(e);
        guiHandleNewSpawn(e);
    }

    private void guiHandleNewSpawn(ItemSpawn spawn) {
        scrollBar.addAllWindowSize(entrySize);
        GuiTextBasedButton button = new GuiTextBasedButton(0, 0, entrySize, entrySize, "-");
        removeButtonMap.put(spawn, button);
        registerGuiComponent(button);
    }

    private void readjustButtonPosition() {

    }

    @Override
    public void moveComponent(int amountX, int amountY) {
        super.moveComponent(amountX, amountY);
        if (itemSelectionScrollTool.isShown()) {
            moveItemSelectionScrollTool();
        }
    }

    public void moveItemSelectionScrollTool() {
        if (x - itemSelectionScrollTool.getWidth() < 0) {
            itemSelectionScrollTool.setPosition(width + 20, 0);
            itemSelectionScrollToolLeft = true;
        } else {
            itemSelectionScrollTool.setPosition(-itemSelectionScrollTool.getWidth(), 0);
            itemSelectionScrollToolLeft = false;
        }
    }

    private void renderItemSpawn(ItemSpawn spawn, float drawX, float drawY, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float partialTicks) {
        dg.getItemRendererRegistry().getRenderer(spawn.getItem()).
                renderItemInScrollTool(spawn.generateStack(), spawn.getItem(), sR, sB, camera, drawX, drawY, entrySize, entrySize, 0, partialTicks);

        sR.begin();
        sR.setColor(Color.BLACK);
        sR.line(drawX, drawY, drawX + width, drawY);
        sR.line(drawX + entrySize, drawY, drawX + entrySize, drawY + entrySize);
        sR.end();
        sB.begin();
        RenderUtils.drawString(I18nUtils.getItemName(spawn.getItem()), dg.defaultFont28, drawX + entrySize + 10, drawY + entrySize / 2, sB, Color.BLACK, false, true);
        sB.end();
    }

    public static class ItemScrollToolEntry implements ScrollToolEntry {

        private Item item;

        public ItemScrollToolEntry(Item item) {
            this.item = item;
        }

        @Override
        public void render(OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float x, float y, float width, float height, boolean highlighted, float partialTicks) {
            int r = highlighted ? 10 : 20;

            IItemRenderer renderer = DuckGamesClient.getDuckGames().getItemRendererRegistry().getRenderer(item);
            Texture itemTexture = renderer.getItemTexture();
            float ratio = itemTexture.getHeight() / ((float) itemTexture.getWidth());
            float rHeight = height * ratio;
            float angle = 0;
            if (ratio < 1) {
                angle = 45;
                r = highlighted ? 0 : 10;
            }
            renderer.renderItemInScrollTool(new ItemStack(item), item, sR, sB, camera, x + r, y + r, width - r * 2, rHeight - r * 2, angle, partialTicks);
        }
    }

    private class AddGuiTextBasedButton extends GuiTextBasedButton {

        public AddGuiTextBasedButton() {
            super(0, 0, 50, ItemSpawnEditorGuiComponent.this.entrySize, "+");
        }

        @Override
        protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(ItemSpawnEditorGuiComponent.this.x), GuiUtils.unscaleScreenCoordY(ItemSpawnEditorGuiComponent.this.y),
                    GuiUtils.unscaleScreenCoordX(ItemSpawnEditorGuiComponent.this.width), GuiUtils.unscaleScreenCoordY(ItemSpawnEditorGuiComponent.this.height - topBarHeight));
            super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }
}
