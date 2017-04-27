package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.data_storage.box.DataBox;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class BasicEntityUpdateInformationPacketToClient implements IPacket {

    public UUID entityUUID;

    public float posX, posY, motionX, motionY, motionMultiplier, health;
    public EntityDirection direction;
    private Entity entity;
    public DataBox additionalUpdateData;

    public BasicEntityUpdateInformationPacketToClient(Entity entity) {
        this.entityUUID = entity.uuid;
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.motionX = entity.motionX;
        this.motionY = entity.motionY;
        this.motionMultiplier = entity.motionMultiplier;
        this.direction = entity.getDirection();
        this.health = entity.getHealth();
        this.entity = entity;
    }

    public BasicEntityUpdateInformationPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, entityUUID);
        out.writeFloat(posX);
        out.writeFloat(posY);
        out.writeFloat(motionX);
        out.writeFloat(motionY);
        out.writeFloat(motionMultiplier);
        out.writeInt(direction.ordinal());
        out.writeFloat(health);
        DataBox box = new DataBox();
        entity.writeAdditionalUpdateData(box);
        box.writeToStream(out);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        entityUUID = NetworkUtils.readUUID(in);
        posX = in.readFloat();
        posY = in.readFloat();
        motionX = in.readFloat();
        motionY = in.readFloat();
        motionMultiplier = in.readFloat();
        direction = EntityDirection.values()[in.readInt()];
        health = in.readFloat();
        additionalUpdateData = new DataBox();
        additionalUpdateData.readFromStream(in);
    }

    public static class Handler implements IPacketHandler<BasicEntityUpdateInformationPacketToClient> {

        @Override
        public void handlePacket(final BasicEntityUpdateInformationPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
