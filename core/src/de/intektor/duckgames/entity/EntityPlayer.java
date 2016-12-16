package de.intektor.duckgames.entity;

import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.world.World;

import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityPlayer extends Entity {

    private PlayerProfile profile;

    public EntityPlayer(World world, float posX, float posY, PlayerProfile profile) {
        super(world, posX, posY);
    }

    public EntityPlayer(UUID uuid) {
        super(uuid);
    }

    @Override
    protected void updateEntity() {

    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }
}
