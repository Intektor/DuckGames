package de.intektor.duckgames.common;

import de.intektor.duckgames.common.entity.EntityPlayerMP;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.world.World;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class ServerProxy implements IProxy {

    private DuckGamesServer server = CommonCode.getDuckGamesServer();

    @Override
    public void handlePacket(IPacket packet, AbstractSocket socketFrom) {

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

    @Override
    public Map<UUID, GameProfile> getGameProfiles() {
        Map<UUID, GameProfile> profileMap = new HashMap<UUID, GameProfile>();
        for (PlayerProfile profile : server.getMainServerThread().getProfileMap().values()) {
            profileMap.put(profile.gameProfile.profileUUID, profile.gameProfile);
        }
        return profileMap;
    }
}
