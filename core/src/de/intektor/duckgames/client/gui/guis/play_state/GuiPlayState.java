package de.intektor.duckgames.client.gui.guis.play_state;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.guis.GuiLevelEditor;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.gui.util.MousePos;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.client.rendering.WorldRenderer;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.client_to_server.*;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
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

    private GuiThumbPad movementPad;

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
        movementPad = new GuiThumbPad(350, 350, 160);
        registerComponent(movementPad);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        if (player == null) return;
        spriteBatch.enableBlending();
        worldRenderer.renderWorld(world, worldCamera, worldShapeRenderer, worldSpriteBatch, player, partialTicks);
        ItemStack equipment = player.getEquipment(EntityEquipmentSlot.MAIN_HAND);
        if (equipment != null && equipment.getItem() instanceof ItemGun) {
            ItemGun gun = (ItemGun) equipment.getItem();
            String ammoInfo = gun.getRemainingBullets(equipment, player, world) + "/" + gun.getRemainingReserveAmmo(equipment, player, world);
            spriteBatch.begin();
            RenderUtils.drawString(ammoInfo, dg.defaultFont28, dg.getPreferredScreenWidth() - FontUtils.getStringWidth(ammoInfo, dg.defaultFont28), FontUtils.getStringHeight(ammoInfo, dg.defaultFont28), spriteBatch, Color.WHITE);
            spriteBatch.end();
        }
        super.renderGui(mouseX, mouseY, camera, partialTicks);
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        world.updateWorld();

        if (input.isTouched()) {
            MousePos mP = GuiUtils.unprojectMousePosition(worldCamera);
            if (player.getEquipment(EntityEquipmentSlot.MAIN_HAND) != null && (mP.x != lastAttackPosX || mP.y != lastAttackPosY)) {
                dg.sendPacketToServer(new PlayerAttackWithItemPacketToServer(mP.x, mP.y, Status.UPDATE));
            }
        }

        dg.sendPacketToServer(new CurrentPadControllingPacketToServer((float) movementPad.getAngle(), (float) movementPad.getPercentage(), 0, 0));
        super.updateGui(mouseX, mouseY);
    }

    @Override
    public void keyPushed(int keyCode, int mouseX, int mouseY) {
        ItemStack mainHand = dg.thePlayer.getEquipment(EntityEquipmentSlot.MAIN_HAND);
        switch (keyCode) {
            case Keys.A:
                dg.sendPacketToServer(new PlayerMovementPacketToServer(true, EnumDirection.LEFT));
                dg.thePlayer.move(EnumDirection.LEFT, true);
                break;
            case Keys.D:
                dg.sendPacketToServer(new PlayerMovementPacketToServer(true, EnumDirection.RIGHT));
                dg.thePlayer.move(EnumDirection.RIGHT, true);
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

    @Override
    protected void pointerUp(int mouseX, int mouseY, int pointer, int button) {
        super.pointerUp(mouseX, mouseY, pointer, button);
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

    @Override
    protected void scrolledWheel(int mouseX, int mouseY, int amount) {
        super.scrolledWheel(mouseX, mouseY, amount);
    }

    @Override
    public void exitGui() {

    }

    @Override
    public void buttonCallback(GuiButton button) {

    }
}
