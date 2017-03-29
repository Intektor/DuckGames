package de.intektor.duckgames.common;

import de.intektor.duckgames.common.entity.EntityPlayerMP;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;
import de.intektor.network.IPacket;

import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Intektor
 */
public class ServerProxy implements IProxy {

    @Override
    public void handlePacket(IPacket packet, Socket socketFrom) {

    }

    @Override
    public World getWorld() {
        return null;
    }

    private Constructor<EntityPlayerMP> playerMPConstructor;

    @Override
    public EntityPlayer createPlayer(World world, UUID uuid) {
        try {
            if (playerMPConstructor == null) {
                playerMPConstructor = EntityPlayerMP.class.getConstructor(UUID.class);
            }
            if (!world.isRemote) {
                return playerMPConstructor.newInstance(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
