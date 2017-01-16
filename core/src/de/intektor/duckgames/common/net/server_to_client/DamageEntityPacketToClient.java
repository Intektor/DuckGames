package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.game.damage.DamageSource;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Intektor
 */
public class DamageEntityPacketToClient implements IPacket {

    public UUID attacked;
    public UUID cause;
    public float damage;

    public DamageEntityPacketToClient(Entity attacked, DamageSource source) {
        this.attacked = attacked.uuid;
        cause = source.getDamageCause().uuid;
        damage = source.getDamage();
    }

    public DamageEntityPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, attacked);
        NetworkUtils.writeUUID(out, cause);
        out.writeFloat(damage);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        attacked = NetworkUtils.readUUID(in);
        cause = NetworkUtils.readUUID(in);
        damage = in.readFloat();
    }

    public static class Handler implements IPacketHandler<DamageEntityPacketToClient> {

        @Override
        public void handlePacket(DamageEntityPacketToClient packet, Socket socketFrom) {
            CommonCode.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}
