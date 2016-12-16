package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.gui.util.GuiUtils;

/**
 * @author Intektor
 */
public class GuiScrollBar extends GuiComponent {

    private Direction direction;
    private int allWindowSize;
    private int canBeShownInWindow;
    private boolean justScrolled;

    private int currentScrollAmt;

    private boolean currentlyClicked;
    private int clickOffsetX, clickOffsetY;

    private DuckGamesClient dg;

    public GuiScrollBar(int x, int y, int width, int height, Direction direction, int allWindowSize, int canBeShownInWindow) {
        super(x, y, width, height);
        this.direction = direction;
        this.allWindowSize = allWindowSize;
        this.canBeShownInWindow = canBeShownInWindow;
        if (direction == Direction.VERTICAL) {
            this.currentScrollAmt = height - getScrollToolSize();
        }
        dg = DuckGamesClient.getDuckGames();
    }

    @Override
    protected void renderComponent(int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB) {
        sR.begin();
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.setColor(Color.LIGHT_GRAY);
        sR.rect(x, y, width, height);

        sR.identity();

        int scrollToolSize = getScrollToolSize();
        sR.setColor(Color.GRAY);
        switch (direction) {
            case HORIZONTAL:
                sR.rect(x + currentScrollAmt, y, scrollToolSize, height);
                break;
            case VERTICAL:
                sR.rect(x, y + currentScrollAmt, width, scrollToolSize);
                break;
        }
        sR.end();
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        super.updateComponent(mouseX, mouseY);
        justScrolled = false;
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button) {
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY)) {
            int scrollToolSize = getScrollToolSize();
            switch (direction) {
                case HORIZONTAL:
                    currentlyClicked = x >= this.x + currentScrollAmt && x <= this.x + currentScrollAmt + scrollToolSize && mouseY >= this.y && mouseY <= this.y + height;
                    clickOffsetX = mouseX - this.x - currentScrollAmt;
                    break;
                case VERTICAL:
                    currentlyClicked = mouseX >= this.x && x <= this.x + width && mouseY >= this.y + currentScrollAmt && mouseY <= this.y + currentScrollAmt + scrollToolSize;
                    clickOffsetY = mouseY - this.y - currentScrollAmt;
                    break;
            }
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button) {
        currentlyClicked = false;
    }

    @Override
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {
        if (currentlyClicked) {
            justScrolled = true;
            float nX = mouseX - x;
            float nY = mouseY - y;
            float percent;
            switch (direction) {
                case HORIZONTAL:
                    percent = (nX - clickOffsetX) / (width);
                    currentScrollAmt = (int) (width * percent);
                    if (currentScrollAmt < 0) {
                        currentScrollAmt = 0;
                        clickOffsetX += mouseX - prevX;
                    }
                    if (currentScrollAmt > width - getScrollToolSize()) {
                        currentScrollAmt = width - getScrollToolSize();
                        clickOffsetX -= prevX - mouseX;
                    }
                    break;
                case VERTICAL:
                    percent = (nY - clickOffsetY) / (height);
                    currentScrollAmt = (int) (height * percent);
                    if (currentScrollAmt < 0) {
                        currentScrollAmt = 0;
                        clickOffsetY += mouseY - prevY;
                    }
                    if (currentScrollAmt > height - getScrollToolSize()) {
                        currentScrollAmt = height - getScrollToolSize();
                        clickOffsetY -= prevY - mouseY;
                    }
                    break;
            }
        }
    }

    public void checkScroll() {
        switch (direction) {
            case HORIZONTAL:
                if (currentScrollAmt < 0) {
                    currentScrollAmt = 0;
                }
                if (currentScrollAmt > width - getScrollToolSize()) {
                    currentScrollAmt = width - getScrollToolSize();
                }
                break;
            case VERTICAL:
                if (currentScrollAmt < 0) {
                    currentScrollAmt = 0;
                }
                if (currentScrollAmt > height - getScrollToolSize()) {
                    currentScrollAmt = height - getScrollToolSize();
                }
                break;
        }
    }

    public float getScrollPercent() {
        switch (direction) {
            case HORIZONTAL:
                return 1 - ((float) currentScrollAmt) / (float) (width - getScrollToolSize());
            case VERTICAL:
                return 1 - ((float) currentScrollAmt) / (float) (height - getScrollToolSize());
        }
        return Float.MIN_VALUE;
    }

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    public int getScrollToolSize() {
        switch (direction) {
            case HORIZONTAL:
                return (int) (width * ((float) canBeShownInWindow / Math.max(allWindowSize, canBeShownInWindow)));
            case VERTICAL:
                return (int) (height * ((float) canBeShownInWindow / Math.max(allWindowSize, canBeShownInWindow)));
        }
        return Integer.MIN_VALUE;
    }

    public void setAllWindowSize(int allWindowSize) {
        this.allWindowSize = allWindowSize;
        if (direction == Direction.VERTICAL) {
            this.currentScrollAmt = height - getScrollToolSize();
        } else {
            this.currentScrollAmt = 0;
        }
    }

    public void addAllWindowSize(int addition) {
        float relation = this.allWindowSize / (this.allWindowSize + (float) addition);
        setScrollPercent(relation);
        this.allWindowSize += addition;
    }

    public int getAllWindowSize() {
        return allWindowSize;
    }


    public void setScrollPercent(float amount) {
        switch (direction) {
            case HORIZONTAL:
                currentScrollAmt = -(int) (1 - amount * (width - getScrollToolSize()));
                break;
            case VERTICAL:
                currentScrollAmt = (int) ((1 - amount) * (height - getScrollToolSize()));
                break;
        }
    }

    public boolean justScrolled() {
        return justScrolled;
    }
}
