package de.intektor.duckgames.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class Collision2D {

    public float x, y;
    public float x2, y2;

    public Collision2D(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        x2 = x + width;
        y2 = y + height;
        update();
    }

    public boolean collidesWith(Collision2D rect) {
        return collides(rect) || rect.collides(this);
    }

    private boolean collides(Collision2D rect) {
        return Intersector.intersectRectangles(toRectangle(), rect.toRectangle(), new Rectangle());
    }

    public boolean isPointInside(Point2f p) {
        return isPointInside(p.x, p.y);
    }

    public boolean isPointInside(float x, float y) {
        return collidesWith(new Collision2D(x, y, 1, 1));
    }

    private void update() {
        if (x2 < x) {
            float xB = x;
            x = x2;
            x2 = xB;
        }
        if (y2 < y) {
            float yB = y;
            y = y2;
            y2 = yB;
        }
    }

    public Collision2D move(float amtX, float amtY) {
        x += amtX;
        y += amtY;
        x2 += amtX;
        y2 += amtY;
        update();
        return this;
    }

    public Collision2D set(float x, float y) {
        float width = getWidth();
        float height = getHeight();
        this.x = x;
        this.y = y;
        x2 = x + width;
        y2 = y + height;
        update();
        return this;
    }

    /**
     * Adds amtX and amtY to width and height
     */
    public Collision2D addSize(float amtX, float amtY) {
        x2 += amtX;
        y2 += amtY;
        update();
        return this;
    }

    public Collision2D grow(float amtX, float amtY) {
        x -= amtX;
        y -= amtY;
        x2 += amtX;
        y2 += amtY;
        update();
        return this;
    }

    public float getWidth() {
        return x2 - x;
    }

    public float getHeight() {
        return y2 - y;
    }

    public Rectangle toRectangle() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    public Collision2D copy() {
        return new Collision2D(x, y, getWidth(), getHeight());
    }
}
