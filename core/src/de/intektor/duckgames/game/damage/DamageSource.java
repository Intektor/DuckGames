package de.intektor.duckgames.game.damage;

import de.intektor.duckgames.entity.Entity;

/**
 * @author Intektor
 */
public class DamageSource {
    
    private Entity damageCause;
    private float damage;

    public DamageSource(Entity damageCause, float damage) {
        this.damageCause = damageCause;
        this.damage = damage;
    }

    public Entity getDamageCause() {
        return damageCause;
    }

    public float getDamage() {
        return damage;
    }
}
