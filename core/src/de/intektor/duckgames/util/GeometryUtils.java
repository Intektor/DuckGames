package de.intektor.duckgames.util;


import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author Intektor
 */
public class GeometryUtils {

    public static Polygon convertRect(Rectangle rectangle) {
        return new Polygon(new float[]{
                rectangle.x, rectangle.y,
                rectangle.x + rectangle.width, rectangle.y,
                rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                rectangle.x, rectangle.y + rectangle.height
        });
    }
}
