package de.intektor.duckgames.common;

import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.world.World;
import de.intektor.network.IPacket;

import java.net.Socket;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public interface IProxy {

    void handlePacket(IPacket packet, Socket socketFrom);

    World getWorld();

    EntityPlayer createPlayer(World world, UUID uuid);

    Map<UUID, GameProfile> getGameProfiles();
}
