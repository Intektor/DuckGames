package de.intektor.duckgames.util.tracing;

import de.intektor.duckgames.entity.Entity;

/**
 * @author Intektor
 */
public class RayTraceResult {

    public final float posX, posY;
    public final Type type;
    public final Entity entityHit;

    public RayTraceResult(float posX, float posY, Type type) {
        this(posX, posY, type, null);
    }

    public RayTraceResult(float posX, float posY, Type type, Entity entityHit) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.entityHit = entityHit;
    }

    public enum Type {
        ENTITY,
        BLOCK
    }
}
