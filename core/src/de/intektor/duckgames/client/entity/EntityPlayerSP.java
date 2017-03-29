package de.intektor.duckgames.client.entity;

import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityPlayerSP extends EntityPlayer {

    public EntityPlayerSP(World world, float posX, float posY, PlayerProfile profile) {
        super(world, posX, posY, profile);
    }

    public EntityPlayerSP(UUID uuid) {
        super(uuid);
    }
}
