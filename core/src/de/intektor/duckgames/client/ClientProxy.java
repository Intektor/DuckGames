package de.intektor.duckgames.client;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.entity.EntityPlayerSP;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.guis.GuiDownloadingMap;
import de.intektor.duckgames.client.gui.guis.GuiMainMenu;
import de.intektor.duckgames.client.gui.guis.lobby.GuiLobby;
import de.intektor.duckgames.client.gui.guis.play_state.GuiPlayState;
import de.intektor.duckgames.common.IProxy;
import de.intektor.duckgames.common.entity.EntityPlayerMP;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.client_to_server.IdentificationPacketToServer;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.game.damage.DamageSource;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.world.World;
import de.intektor.duckgames.world.WorldClient;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class ClientProxy implements IProxy {

    private final DuckGamesClient duckGames = DuckGamesClient.getDuckGames();

    @Override
    public void handlePacket(IPacket packet, AbstractSocket socketFrom) {
        if (packet instanceof BasicEntityUpdateInformationPacketToClient) {
            handleBasicEntityUpdateInformationPacketToClient((BasicEntityUpdateInformationPacketToClient) packet);
        } else if (packet instanceof DamageEntityPacketToClient) {
            handleDamageEntityPacketToClient((DamageEntityPacketToClient) packet);
        } else if (packet instanceof DropEquipmentItemStackPacketToClient) {
            handleDropEquipmentItemStackPacketToClient((DropEquipmentItemStackPacketToClient) packet);
        } else if (packet instanceof FinishedWorldTransmissionPacketToClient) {
            handleFinishedWorldTransmissionPacketToClient((FinishedWorldTransmissionPacketToClient) packet);
        } else if (packet instanceof IdentificationSuccessfulPacketToClient) {
            handleIdentificationSuccessfulPacketToClient((IdentificationSuccessfulPacketToClient) packet);
        } else if (packet instanceof KickClientFromServerPacketToClient) {
            handleKickClientFromServerPacketToClient((KickClientFromServerPacketToClient) packet);
        } else if (packet instanceof PickupEquipmentItemStackPacketToClient) {
            handlePickupEquipmentItemStackPacketToClient((PickupEquipmentItemStackPacketToClient) packet);
        } else if (packet instanceof PlaceBlockPacketToClient) {
            handlePlaceBlockPacketToClient((PlaceBlockPacketToClient) packet);
        } else if (packet instanceof PlayerAttackWithItemPacketToClient) {
            handlePlayerAttackWithItemPacketToClient((PlayerAttackWithItemPacketToClient) packet);
        } else if (packet instanceof PlayerPacketToClient) {
            handlePlayerPacketToClient((PlayerPacketToClient) packet);
        } else if (packet instanceof RemoveEntityPacketToClient) {
            handleRemoveEntityPacketToClient((RemoveEntityPacketToClient) packet);
        } else if (packet instanceof RequestIdentificationPacketToClient) {
            handleRequestIdentificationPacketToClient((RequestIdentificationPacketToClient) packet);
        } else if (packet instanceof SpawnEntityPacketToClient) {
            handleSpawnEntityPacketToClient((SpawnEntityPacketToClient) packet);
        } else if (packet instanceof WorldPacketToClient) {
            handleWorldPacketToClient((WorldPacketToClient) packet);
        } else if (packet instanceof UpdateEquipmentPacketToClient) {
            handleUpdateEquipmentPacketToClient((UpdateEquipmentPacketToClient) packet);
        } else if (packet instanceof ChatMessagePacketToClient) {
            handleChatMessagePacketToClient((ChatMessagePacketToClient) packet);
        } else if (packet instanceof PlayerProfilesPacketToClient) {
            handlePlayerProfilesPacketToClient((PlayerProfilesPacketToClient) packet);
        } else if (packet instanceof GameScorePacketToClient) {
            handleGameScorePacketToClient((GameScorePacketToClient) packet);
        } else if (packet instanceof RoundEndedPacketToClient) {
            handleRoundEndedPacketToClient((RoundEndedPacketToClient) packet);
        } else if (packet instanceof LobbyChangeMapPacketToClient) {
            handleLobbyChangeMapPacketToClient((LobbyChangeMapPacketToClient) packet);
        } else if (packet instanceof RemoveProfilePacketToClient) {
            handleRemoveProfilePacketToClient((RemoveProfilePacketToClient) packet);
        } else if (packet instanceof NewRoundPacketToClient) {
            handleNewRoundPacketToClient((NewRoundPacketToClient) packet);
        }
    }

    @Override
    public World getWorld() {
        return duckGames.theWorld;
    }

    private Constructor<EntityPlayerSP> playerSPConstructor;
    private Constructor<EntityPlayerMP> playerMPConstructor;

    @Override
    public EntityPlayer createPlayer(World world, UUID uuid) {
        try {
            if (playerSPConstructor == null) {
                playerSPConstructor = EntityPlayerSP.class.getConstructor(UUID.class);
                playerMPConstructor = EntityPlayerMP.class.getConstructor(UUID.class);
            }
            if (world.isRemote) {
                return playerSPConstructor.newInstance(uuid);
            } else {
                return playerMPConstructor.newInstance(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<UUID, GameProfile> getGameProfiles() {
        return duckGames.getClientConnection().getPlayerProfiles();
    }

    private void handleBasicEntityUpdateInformationPacketToClient(final BasicEntityUpdateInformationPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                WorldClient theWorld = duckGames.theWorld;
                Entity entity = theWorld.getEntityByUUID(packet.entityUUID);
                if (entity == null) return;
                entity.prevPosX = entity.posX;
                entity.prevPosY = entity.posY;
                entity.posX = packet.posX;
                entity.posY = packet.posY;
                entity.motionX = packet.motionX;
                entity.motionY = packet.motionY;
                entity.motionMultiplier = packet.motionMultiplier;
                entity.adjustCollision();
                entity.setDirection(packet.direction);
                entity.setHealth(packet.health);
                entity.readAdditionalUpdateData(packet.additionalUpdateData);
            }
        });
    }

    private void handleDamageEntityPacketToClient(final DamageEntityPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Entity attackedEntity = duckGames.theWorld.getEntityByUUID(packet.attacked);
                Entity attacker = duckGames.theWorld.getEntityByUUID(packet.cause);
                attackedEntity.damageEntity(new DamageSource(attacker, packet.damage));
            }
        });
    }

    private void handleDropEquipmentItemStackPacketToClient(final DropEquipmentItemStackPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EntityPlayer player = (EntityPlayer) duckGames.theWorld.getEntityByUUID(packet.entityUUID);
                ItemStack equipment = player.getEquipment(packet.slot);
                equipment.getItem().onItemThrownAway(equipment, player, player.world);
                player.setEquipment(packet.slot, null);
            }
        });
    }

    private void handleFinishedWorldTransmissionPacketToClient(final FinishedWorldTransmissionPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.showGui(new GuiPlayState());
            }
        });
    }

    private void handleIdentificationSuccessfulPacketToClient(final IdentificationSuccessfulPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.getClientConnection().setIdentificationSuccessful(true);
            }
        });
    }

    private void handleKickClientFromServerPacketToClient(final KickClientFromServerPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.disconnect();
                duckGames.showGui(new GuiMainMenu());
            }
        });
    }

    private void handlePickupEquipmentItemStackPacketToClient(final PickupEquipmentItemStackPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EntityPlayer player = (EntityPlayer) duckGames.theWorld.getEntityByUUID(packet.entityID);
                player.setEquipment(packet.slot, packet.pickedStack);
                packet.pickedStack.getItem().onItemPickup(packet.pickedStack, player, player.world);
            }
        });
    }

    private void handlePlaceBlockPacketToClient(final PlaceBlockPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.theWorld.setBlock(packet.x, packet.y, packet.block);
            }
        });
    }

    private void handlePlayerAttackWithItemPacketToClient(final PlayerAttackWithItemPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EntityPlayer player = (EntityPlayer) duckGames.theWorld.getEntityByUUID(packet.playerID);
                ItemStack mainHand = player.getEquipment(EntityEquipmentSlot.MAIN_HAND);
                if (mainHand != null) {
                    player.setAttacking(packet.status);
                    switch (packet.status) {
                        case START:
                            mainHand.getItem().onAttackWithItemBegin(mainHand, player, player.world, packet.ingameClickX, packet.ingameClickY);
                            break;
                        case END:
                            mainHand.getItem().onAttackWithItemEnd(mainHand, player, player.world, packet.ingameClickX, packet.ingameClickY);
                            break;
                    }
                }
            }
        });
    }

    private void handlePlayerPacketToClient(final PlayerPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.thePlayer = (EntityPlayer) duckGames.theWorld.getEntityByUUID(packet.playerUUID);
            }
        });
    }

    private void handleRemoveEntityPacketToClient(final RemoveEntityPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Entity entity = duckGames.theWorld.getEntityByUUID(packet.entity);
                entity.isDead = true;
                duckGames.theWorld.removeEntity(entity);
            }
        });
    }

    private void handleRequestIdentificationPacketToClient(final RequestIdentificationPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.sendPacketToServer(new IdentificationPacketToServer(duckGames.getUsername()));
            }
        });
    }

    private void handleSpawnEntityPacketToClient(final SpawnEntityPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.theWorld.spawnEntityInWorld(packet.entity);
            }
        });
    }

    private void handleWorldPacketToClient(final WorldPacketToClient packet) {
        duckGames.theWorld = new WorldClient(packet.blockTable, packet.width, packet.height);
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.showGui(new GuiDownloadingMap());
            }
        });
    }

    private void handleUpdateEquipmentPacketToClient(final UpdateEquipmentPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EntityPlayer player = (EntityPlayer) duckGames.theWorld.getEntityByUUID(packet.playerUUID);
                player.setEquipment(packet.slot, packet.stack);
            }
        });
    }

    private void handleChatMessagePacketToClient(final ChatMessagePacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                if (duckGames.getCurrentGui() instanceof GuiLobby) {
                    GuiLobby lobby = (GuiLobby) duckGames.getCurrentGui();
                    lobby.addMessage(packet.message);
                }
            }
        });
    }

    private void handlePlayerProfilesPacketToClient(final PlayerProfilesPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.getClientConnection().getPlayerProfiles().put(packet.profile.profileUUID, packet.profile);
            }
        });
    }

    private void handleGameScorePacketToClient(final GameScorePacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                duckGames.getClientConnection().setCurrentGameScore(packet.score);
            }
        });
    }

    public void handleRoundEndedPacketToClient(final RoundEndedPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Gui currentGui = duckGames.getCurrentGui();
                if (currentGui instanceof GuiPlayState) {
                    GuiPlayState gui = (GuiPlayState) currentGui;
                    gui.roundEnded(packet);
                }
            }
        });
    }

    public void handleLobbyChangeMapPacketToClient(final LobbyChangeMapPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Gui currentGui = duckGames.getCurrentGui();
                if (currentGui instanceof GuiLobby) {
                    GuiLobby gui = (GuiLobby) currentGui;
                    gui.setSelectedMapName(packet.mapName);
                }
            }
        });
    }

    public void handleRemoveProfilePacketToClient(final RemoveProfilePacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                getGameProfiles().remove(packet.profile.profileUUID);
            }
        });
    }

    public void handleNewRoundPacketToClient(final NewRoundPacketToClient packet) {
        duckGames.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Gui currentGui = duckGames.getCurrentGui();
                if (currentGui instanceof GuiPlayState) {
                    GuiPlayState gui = (GuiPlayState) currentGui;
                    gui.newRound(packet);
                }
            }
        });
    }
}
