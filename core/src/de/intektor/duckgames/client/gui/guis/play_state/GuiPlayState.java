package de.intektor.duckgames.client.gui.guis.play_state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.DeviceUtils;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiImageBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.guis.GuiLevelEditor;
import de.intektor.duckgames.client.gui.guis.GuiMainMenu;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.gui.util.MousePos;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.client.rendering.WorldRenderer;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.client_to_server.*;
import de.intektor.duckgames.common.net.server_to_client.RoundEndedPacketToClient;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.game.GameScore;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.item.items.gun.ItemGun;
import de.intektor.duckgames.util.EnumDirection;
import de.intektor.duckgames.world.World;

import java.io.IOException;

/**
 * @author Intektor
 */
public class GuiPlayState extends Gui {

    private WorldRenderer worldRenderer = new WorldRenderer();

    private OrthographicCamera worldCamera;
    private ShapeRenderer worldShapeRenderer;
    private SpriteBatch worldSpriteBatch;

    private float lastAttackPosX, lastAttackPosY;

    private GuiImageBasedButton buttonMenu;
    private GuiTextBasedButton buttonMenuContinue;
    private GuiTextBasedButton buttonMenuChat;
    private GuiTextBasedButton buttonMenuExit;

    private boolean menuShown;

    private GuiThumbPad movementPad;
    private GuiThumbPad aimingPad;

    private GameProfile winningProfile;
    private long timeShowingWinner;

    private boolean touchMode;

    private boolean postMortemMode;
    private EntityPlayer currentlySpectating;


    private static final Texture menuButtonTexture = new Texture(Gdx.files.internal("menu_button.png"));

    public GuiPlayState() {
        worldCamera = new OrthographicCamera(width, height);
        worldCamera.position.set(width / 2, height / 2, 0);
        worldShapeRenderer = new ShapeRenderer();
        worldShapeRenderer.setAutoShapeType(true);
        worldSpriteBatch = new SpriteBatch();
        dg.theWorld.updateWorld();
    }

    @Override
    public void enterGui() {
        super.enterGui();

        touchMode = DeviceUtils.isDeviceTouch();

        buttonMenu = new GuiImageBasedButton(width - 100, height - 90, 80, 60, menuButtonTexture);

        Color color = new Color(0.2f, 0.2f, 0.2f, 0.5f);
        Color hoveredColor = new Color(0.25f, 0.25f, 0.25f, 0.5f);
        buttonMenuContinue = new GuiTextBasedButton(width / 2 - width / 6, height / 2 + 50, width / 3, 100, "Continue", true, color, hoveredColor, Color.WHITE);
        buttonMenuChat = new GuiTextBasedButton(width / 2 - width / 6, height / 2 - 50, width / 3, 100, "Open Chat", true, color, hoveredColor, Color.WHITE);
        buttonMenuExit = new GuiTextBasedButton(width / 2 - width / 6, height / 2 - 150, width / 3, 100, "Exit", true, color, hoveredColor, Color.WHITE);

        registerComponent(buttonMenu);
        registerComponent(buttonMenuContinue);
        registerComponent(buttonMenuChat);
        registerComponent(buttonMenuExit);

        showMenu(false);

        if (touchMode) {
            movementPad = new GuiThumbPad(350, 350, 160);
            aimingPad = new GuiThumbPad(width - 350, 350, 160);

            registerComponent(movementPad);
            registerComponent(aimingPad);
        }

    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        if (player == null) return;

        spriteBatch.enableBlending();

        EntityPlayer watchingPlayer = postMortemMode ? currentlySpectating : player;

        worldRenderer.renderWorld(world, worldCamera, worldShapeRenderer, worldSpriteBatch, watchingPlayer, partialTicks);

        ItemStack equipment = player.getEquipment(EntityEquipmentSlot.MAIN_HAND);
        if (equipment != null && equipment.getItem() instanceof ItemGun) {
            ItemGun gun = (ItemGun) equipment.getItem();
            String ammoInfo = gun.getRemainingBullets(equipment, player, world) + "/" + gun.getRemainingReserveAmmo(equipment, player, world);
            spriteBatch.begin();
            RenderUtils.drawString(ammoInfo, dg.defaultFont28, dg.getPreferredScreenWidth() - FontUtils.getStringWidth(ammoInfo, dg.defaultFont28), FontUtils.getStringHeight(ammoInfo, dg.defaultFont28), spriteBatch, Color.WHITE);
            spriteBatch.end();
        }

        spriteBatch.begin();

        BitmapFont font = dg.defaultFont28;

        float y = height - font.getLineHeight() * 3;
        for (GameProfile profile : CommonCode.proxy.getGameProfiles().values()) {
            GameScore.PlayerScore scoreForProfile = dg.getClientConnection().getCurrentGameScore().getScoreForProfile(profile);
            if (scoreForProfile != null) {
                RenderUtils.drawString(String.format("%s: %s", profile.username, scoreForProfile.getWonRounds()), font, 0, y, spriteBatch, Color.WHITE);
                y -= font.getLineHeight();
            }
        }

        if (winningProfile != null) {
            if (dg.theWorld.getWorldTime() - timeShowingWinner <= 60) {
                RenderUtils.drawString(String.format("%s won the round!", winningProfile.username), dg.defaultFont72, width / 2, height / 2 + 200, spriteBatch, Color.WHITE, true);
            } else {
                winningProfile = null;
            }
        }

        spriteBatch.end();

        spriteBatch.enableBlending();

        super.renderGui(mouseX, mouseY, camera, partialTicks);
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        if (player == null) return;

        world.updateWorld();

        if (player.isDead && !postMortemMode) {
            postMortemMode = true;
            currentlySpectating = player;
        }

        if (!menuShown) {
            if (input.isTouched() && !touchMode) {
                MousePos mP = GuiUtils.unprojectMousePosition(worldCamera);
                if (player.getEquipment(EntityEquipmentSlot.MAIN_HAND) != null && (mP.x != lastAttackPosX || mP.y != lastAttackPosY)) {
                    dg.sendPacketToServer(new PlayerAttackWithItemPacketToServer(mP.x, mP.y, Status.UPDATE));
                }
            }

            if (touchMode)
                dg.sendPacketToServer(new CurrentPadControllingPacketToServer((float) movementPad.getAngle(), (float) movementPad.getPercentage(), (float) aimingPad.getAngle(), (float) aimingPad.getPercentage()));
        }
        super.updateGui(mouseX, mouseY);
    }

    @Override
    public void keyPushed(int keyCode, int mouseX, int mouseY) {
        if (menuShown) return;
        ItemStack mainHand = dg.thePlayer.getEquipment(EntityEquipmentSlot.MAIN_HAND);
        switch (keyCode) {
            case Keys.A:
                dg.sendPacketToServer(new PlayerMovementPacketToServer(true, EnumDirection.LEFT));
                break;
            case Keys.D:
                dg.sendPacketToServer(new PlayerMovementPacketToServer(true, EnumDirection.RIGHT));
                break;
            case Keys.SPACE:
                dg.sendPacketToServer(new JumpPacketToServer(true));
                break;
            case Keys.ESCAPE:
                try {
                    dg.disconnect();
                    dg.getDedicatedServer().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dg.showGui(new GuiLevelEditor(dg.getDedicatedServer().getMainServerThread().getBackup()));
                break;
            case Keys.Q:
                if (dg.thePlayer.getEquipment(EntityEquipmentSlot.MAIN_HAND) != null) {
                    dg.sendPacketToServer(new PlayerDropItemPacketToServer(EntityEquipmentSlot.MAIN_HAND));
                }
                break;
            case Keys.R:
                if (mainHand != null && mainHand.getItem() instanceof ItemGun) {
                    dg.sendPacketToServer(new ReloadPacketToServer());
                }
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode, int mouseX, int mouseY) {
        if (menuShown) return;
        switch (keyCode) {
            case Keys.A:
                dg.sendPacketToServer(new PlayerMovementPacketToServer(false, EnumDirection.LEFT));
                dg.thePlayer.move(EnumDirection.LEFT, false);
                break;
            case Keys.D:
                dg.sendPacketToServer(new PlayerMovementPacketToServer(false, EnumDirection.RIGHT));
                dg.thePlayer.move(EnumDirection.RIGHT, false);
                break;
            case Keys.SPACE:
                dg.sendPacketToServer(new JumpPacketToServer(false));
                break;
        }
    }

    @Override
    public void charTyped(char character, int mouseX, int mouseY) {
        super.charTyped(character, mouseX, mouseY);
    }

    @Override
    protected void pointerDown(int mouseX, int mouseY, int pointer, int button) {
        super.pointerDown(mouseX, mouseY, pointer, button);
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        if (menuShown) return;
        if (player == null || world == null) return;

        if (postMortemMode) {
            int i = world.getPlayerList().indexOf(currentlySpectating) + 1;
            i = i == world.getPlayerList().size() ? 0 : i;
            currentlySpectating = world.getPlayerList().get(i);
        }

        if (!touchMode) {
            MousePos mP = GuiUtils.unprojectMousePosition(worldCamera);
            switch (button) {
                case 0:
                    if (player.getEquipment(EntityEquipmentSlot.MAIN_HAND) != null) {
                        dg.sendPacketToServer(new PlayerAttackWithItemPacketToServer(mP.x, mP.y, Status.START));
                        lastAttackPosX = mP.x;
                        lastAttackPosY = mP.y;
                    }
                    break;
                case 1:

                    break;
            }
        }
    }

    @Override
    protected void pointerUp(int mouseX, int mouseY, int pointer, int button) {
        super.pointerUp(mouseX, mouseY, pointer, button);
        if (touchMode || menuShown) return;
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        MousePos mP = GuiUtils.unprojectMousePosition(worldCamera);
        switch (button) {
            case 0:
                if (player.getEquipment(EntityEquipmentSlot.MAIN_HAND) != null) {
                    dg.sendPacketToServer(new PlayerAttackWithItemPacketToServer(mP.x, mP.y, Status.END));
                    lastAttackPosX = mP.x;
                    lastAttackPosY = mP.y;
                }
                break;
            case 1:

                break;
        }
    }

    @Override
    protected void pointerDragged(int mouseX, int mouseY, int prevMouseX, int prevMouseY, int pointer) {
        super.pointerDragged(mouseX, mouseY, prevMouseX, prevMouseY, pointer);
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
    }

    @Override
    protected void pointerMoved(int mouseX, int mouseY, int prevMouseX, int prevMouseY) {
        super.pointerMoved(mouseX, mouseY, prevMouseX, prevMouseY);
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;

    }

    /**
     * Called by {@link de.intektor.duckgames.client.ClientProxy}
     */
    public void roundEnded(RoundEndedPacketToClient packet) {
        timeShowingWinner = dg.theWorld.getWorldTime();
        winningProfile = packet.winner;
    }

    @Override
    protected void scrolledWheel(int mouseX, int mouseY, int amount) {
        super.scrolledWheel(mouseX, mouseY, amount);
    }

    @Override
    public void exitGui() {

    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == buttonMenu) {
            showMenu(true);
        } else if (button == buttonMenuContinue) {
            showMenu(false);
        } else if (button == buttonMenuChat) {
            ;
        } else if (button == buttonMenuExit) {
            DuckGamesServer server = CommonCode.getDuckGamesServer();
            if (server == null) {
                dg.sendPacketToServer(new DisconnectPacketToServer());
                dg.disconnect();
                dg.showGui(new GuiMainMenu());
            } else {
                dg.disconnect();
                if (server.getMainServerThread().getGameMode() == DuckGamesServer.GameMode.TEST_WORLD) {
                    dg.showGui(new GuiLevelEditor(dg.getDedicatedServer().getMainServerThread().getBackup()));
                } else {
                    dg.showGui(new GuiMainMenu());
                }
                dg.setDedicatedServer(null);
            }
        }
    }

    private void showMenu(boolean show) {
        menuShown = show;
        buttonMenuContinue.setShown(show);
        buttonMenuChat.setShown(show);
        buttonMenuExit.setShown(show);
    }
}
