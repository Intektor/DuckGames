package de.intektor.duckgames.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author Intektor
 */
public class CollisionRect {

    public float x, y;
    public float width, height;

    public CollisionRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean collidesWith(CollisionRect rect) {
        return Intersector.intersectRectangles(toRectangle(), rect.toRectangle(), new Rectangle());
    }

    public void move(float amtX, float amtY) {
        x += amtX;
        y += amtY;
    }

    /**
     * Adds amtX and amtY to width and height
     */
    public void addSize(float amtX, float amtY) {
        width += amtX;
        height += amtY;
    }

    public void grow(float amtX, float amtY) {
        x -= amtX;
        y -= amtY;
        width += amtX;
        height += amtY;
    }

    public Rectangle toRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public CollisionRect copy() {
        return new CollisionRect(x, y, width, height);
    }
}
