package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiMultiComponent;
import de.intektor.duckgames.client.gui.components.GuiScrollBar.Direction;
import de.intektor.duckgames.client.gui.util.GuiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class GuiScrollTool<T extends GuiScrollTool.ScrollToolEntry> extends GuiMultiComponent {

    protected GuiScrollBar verticalScrollBar;
    protected GuiScrollBar horizontalScrollBar;

    protected boolean isTouch;

    protected int entryWidth;
    protected int entryHeight;
    protected int entryColumns;

    protected int offsetX;
    protected int offsetY;

    protected List<T> entryList = new ArrayList<T>();

    private ScrollToolCallback callback;

    public GuiScrollTool(int x, int y, int width, int height, boolean verticalScroll, boolean horizontalScroll, boolean touch, int entryWidth, int entryHeight, int entryColumns, ScrollToolCallback callback) {
        super(x, y, width, height);
        this.isTouch = touch;
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
        this.entryColumns = entryColumns;
        if (!touch) {
            if (verticalScroll) {
                verticalScrollBar = new GuiScrollBar(-20, 0, 20, height, Direction.VERTICAL, 20, height);
                registerGuiComponent(verticalScrollBar);
            }
            if (horizontalScroll) {
                horizontalScrollBar = new GuiScrollBar(0, height, width, 20, Direction.HORIZONTAL, 1, width);
                registerGuiComponent(horizontalScrollBar);
            }
        }
        this.callback = callback;
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        sR.identity();
        sR.begin();
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.setColor(Color.BLUE);
        sR.rect(drawX, drawY, width, height);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(drawX), GuiUtils.unscaleScreenCoordY(drawY), GuiUtils.unscaleScreenCoordX(width), GuiUtils.unscaleScreenCoordY(height));
        int[] rac = calculateRowsAndColumns(entryList.size());
        int i = 0;
        int currentRow = 0;
        int currentColumn = -1;
        if (!isTouch) {
            offsetX = horizontalScrollBar != null ? (int) (horizontalScrollBar.getScrollPercent() * (rac[1] - 1) * entryWidth - width) : 0;
            offsetY = verticalScrollBar != null ? -(int) (verticalScrollBar.getScrollPercent() * ((rac[0] - 1) * entryHeight - height)) : 0;
        }
        sR.end();
        while (i < entryList.size()) {
            currentColumn++;
            int x = (int) (drawX + offsetX + entryWidth * currentColumn);
            int y = (int) (drawY + height - (offsetY + entryHeight * currentRow + entryHeight));
            boolean highlighted = GuiUtils.isPointInRegion(x, y, entryWidth, entryHeight, GuiUtils.scaleMouseX(), GuiUtils.scaleMouseY());
            entryList.get(i).
                    render(camera, sR, sB, x, y, entryWidth, entryHeight, highlighted, partialTicks);
            if (currentColumn + 1 == entryColumns) {
                currentColumn = -1;
                currentRow++;
            }
            i++;
        }
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    @Override
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {
        super.clickDragged(mouseX, mouseY, prevX, prevY, pointer);
        if (isTouch) {
            int dX = mouseX - prevX;
            int dY = mouseY - prevY;
            offsetX += dX;
            offsetY -= dY;

            int[] rac = calculateRowsAndColumns(entryList.size());
            int maxX = -entryWidth * (rac[1]) + width;
            if (offsetX < maxX) offsetX = maxX;
            if (offsetX > 0) offsetX = 0;
            if (offsetY > 0) offsetY = 0;
        }
    }

    public void addEntry(T entry) {
        entryList.add(entry);
        int[] i = calculateRowsAndColumns(entryList.size());
        if (!isTouch) {
            if (horizontalScrollBar != null) horizontalScrollBar.setAllWindowSize(entryWidth * (i[1]));
            if (verticalScrollBar != null) verticalScrollBar.setAllWindowSize(entryHeight * (i[0] - 1));
        }
    }

    private ScrollToolEntry clickedEntry;

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        clickedEntry = getEntry(GuiUtils.scaleMouseX(), GuiUtils.scaleMouseY(), drawX, drawY);
    }

    protected ScrollToolEntry getEntry(int mouseX, int mouseY, float drawX, float drawY) {
        int i = 0;
        int currentRow = 0;
        int currentColumn = -1;
        while (i < entryList.size()) {
            currentColumn++;
            int x = (int) (drawX + offsetX + entryWidth * currentColumn);
            int y = (int) (drawY + height - (offsetY + entryHeight * currentRow + entryHeight));
            if (GuiUtils.isPointInRegion(x, y, entryWidth, entryHeight, GuiUtils.scaleMouseX(), GuiUtils.scaleMouseY()))
                return entryList.get(i);
            if (currentColumn + 1 == entryColumns) {
                currentColumn = -1;
                currentRow++;
            }
            i++;
        }
        return null;
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
        if (getEntry(mouseX, mouseY, drawX, drawY) == clickedEntry && clickedEntry != null) {
            callback.scrollToolCallback(this, clickedEntry);
            clickedEntry = null;
        }
    }

    protected int[] calculateRowsAndColumns(int elements) {
        int row = 1;
        int maxColumn = 0;
        int columns = 0;
        for (int i = 0; i < elements; i++) {
            columns++;
            if (columns == entryColumns) {
                row++;
                maxColumn = columns;
                columns = 0;
            }
        }
        return new int[]{row, maxColumn};
    }

    public interface ScrollToolEntry {
        void render(OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float x, float y, float width, float height, boolean highlighted, float partialTicks);
    }

    public interface ScrollToolCallback {
        void scrollToolCallback(GuiScrollTool tool, ScrollToolEntry entry);
    }
}
