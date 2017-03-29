package de.intektor.duckgames.common.entity;

import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityPlayerMP extends EntityPlayer {

    public EntityPlayerMP(World world, float posX, float posY, PlayerProfile profile) {
        super(world, posX, posY, profile);
    }

    public EntityPlayerMP(UUID uuid) {
        super(uuid);
    }
}
