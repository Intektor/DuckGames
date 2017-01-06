package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.Entity;
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
public class BasicEntityUpdateInformationPacketToClient implements IPacket {

    public UUID entityUUID;

    public float posX, posY, prevPosX, prevPosY, motionX, motionY, motionMultiplier, health;
    public boolean isLeft, isRight;

    public BasicEntityUpdateInformationPacketToClient(Entity entity) {
        this.entityUUID = entity.uuid;
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.prevPosX = entity.prevPosX;
        this.prevPosY = entity.prevPosY;
        this.motionX = entity.motionX;
        this.motionY = entity.motionY;
        this.motionMultiplier = entity.motionMultiplier;
        this.isLeft = entity.isLeft();
        this.isRight = entity.isRight();
        this.health = entity.getHealth();
    }

    public BasicEntityUpdateInformationPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, entityUUID);
        out.writeFloat(posX);
        out.writeFloat(posY);
        out.writeFloat(prevPosX);
        out.writeFloat(prevPosY);
        out.writeFloat(motionX);
        out.writeFloat(motionY);
        out.writeFloat(motionMultiplier);
        out.writeBoolean(isLeft);
        out.writeBoolean(isRight);
        out.writeFloat(health);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        entityUUID = NetworkUtils.readUUID(in);
        posX = in.readFloat();
        posY = in.readFloat();
        prevPosX = in.readFloat();
        prevPosY = in.readFloat();
        motionX = in.readFloat();
        motionY = in.readFloat();
        motionMultiplier = in.readFloat();
        isLeft = in.readBoolean();
        isRight = in.readBoolean();
        health = in.readFloat();
    }

    public static class Handler implements IPacketHandler<BasicEntityUpdateInformationPacketToClient> {

        @Override
        public void handlePacket(final BasicEntityUpdateInformationPacketToClient packet, Socket socketFrom) {
            SharedGameRegistries.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}