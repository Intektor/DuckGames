package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.editor.EntitySpawn;
import de.intektor.duckgames.client.editor.EntitySpawn.EntitySpawnType;
import de.intektor.duckgames.client.editor.EntitySpawnCreationRegistry;
import de.intektor.duckgames.client.editor.components.ItemSpawnEditorGuiComponent;
import de.intektor.duckgames.client.editor.components.SaveWorldGuiComponent;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiImageBasedButton;
import de.intektor.duckgames.client.gui.components.GuiScrollTool;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.gui.util.MousePos;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.HostingInfo;
import de.intektor.duckgames.common.HostingType;
import de.intektor.duckgames.game.worlds.spawns.ItemSpawner;
import de.intektor.duckgames.game.worlds.spawns.PlayerSpawn;
import de.intektor.duckgames.game.worlds.spawns.renderer.EntitySpawnRendererRegistry;
import de.intektor.duckgames.game.worlds.spawns.renderer.ItemSpawnRenderer;
import de.intektor.duckgames.game.worlds.spawns.renderer.PlayerSpawnRenderer;

import java.net.InetSocketAddress;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * @author Intektor
 */
public class GuiLevelEditor extends Gui implements GuiScrollTool.ScrollToolCallback {

    private EditableGameMap map;

    private OrthographicCamera editorCamera;
    private ShapeRenderer editorShapeRenderer;
    private SpriteBatch editorSpriteBatch;

    private GuiScrollTool<BlockScrollToolEntry> blockBuildTool;
    private GuiScrollTool<EntitySpawnToolEntry> entitySpawnTool;

    private ItemSpawnEditorGuiComponent itemSpawnEditorGuiComponent;

    private SaveWorldGuiComponent saveWorldGuiComponent;

    private GuiImageBasedButton buttonMenu;

    private GuiTextBasedButton menuButtonContinue;
    private GuiTextBasedButton menuButtonSaveWorld;
    private GuiTextBasedButton menuButtonTestWorld;
    private GuiTextBasedButton menuButtonExitLevelEditor;

    private Block currentSelectedBlock;
    private EntitySpawnToolEntry currentSelectedSpawnType;

    private EditorTool currentTool;

    private boolean waitingForTestServer;
    private boolean waitingForClientConnection;

    private float lastInEditorClickX;
    private float lastInEditorClickY;
    private boolean lastClickInEditor;

    private boolean deleteEntitySpawnToolSelecting;

    private final int toolBarHeight = 200;

    private EntitySpawn selectedEntitySpawn;
    private EntitySpawn grabbedEntitySpawn;

    private boolean showGrid = true;

    private boolean menuShown;

    private static EntitySpawnCreationRegistry entitySpawnCreationRegistry;
    private static EntitySpawnRendererRegistry entitySpawnRendererRegistry;

    private static final Texture trashCanTexture = new Texture("trash_can.png");
    private static final Texture cursorTexture = new Texture("cursor.png");
    private static final Texture grabTexture = new Texture("grab.png");
    public static final Texture menuButtonTexture = new Texture(Gdx.files.internal("menu_button.png"));

    static {
        entitySpawnCreationRegistry = new EntitySpawnCreationRegistry();
        entitySpawnCreationRegistry.register(EntitySpawnType.PLAYER_SPAWN, PlayerSpawn.class);
        entitySpawnCreationRegistry.register(EntitySpawnType.ITEM_SPAWN, ItemSpawner.class);

        entitySpawnRendererRegistry = new EntitySpawnRendererRegistry();
        entitySpawnRendererRegistry.register(PlayerSpawn.class, new PlayerSpawnRenderer());
        entitySpawnRendererRegistry.register(ItemSpawner.class, new ItemSpawnRenderer());
    }

    public GuiLevelEditor(EditableGameMap map) {
        this.map = map;
        editorCamera = new OrthographicCamera(width, height);
        editorShapeRenderer = new ShapeRenderer();
        editorShapeRenderer.setAutoShapeType(true);
        editorSpriteBatch = new SpriteBatch();
        currentSelectedBlock = Blocks.DIRT;
        currentTool = EditorTool.PLACE_BLOCK;
        editorCamera.position.set(map.getWidth() / 2, map.getHeight() / 2 - 2, 0);
        editorCamera.zoom = 0.03f;
        editorCamera.update();
    }

    @Override
    public void enterGui() {
        super.enterGui();
        blockBuildTool = new GuiScrollTool<BlockScrollToolEntry>(50, 150, 500, 600, true, false, false, 100, 100, 5, this);
        for (Block block : CommonCode.gameRegistry.getAllRegisteredBlocks()) {
            blockBuildTool.addEntry(new BlockScrollToolEntry(block));
        }

        registerComponent(blockBuildTool);

        blockBuildTool.setShown(false);
        blockBuildTool.setEnabled(false);

        entitySpawnTool = new GuiScrollTool<EntitySpawnToolEntry>(250, 150, 500, 600, true, false, false, 100, 100, 7, this);
        currentSelectedSpawnType = new EntitySpawnToolEntry(EntitySpawnType.PLAYER_SPAWN);
        entitySpawnTool.addEntry(currentSelectedSpawnType);
        entitySpawnTool.addEntry(new EntitySpawnToolEntry(EntitySpawnType.ITEM_SPAWN));

        registerComponent(entitySpawnTool);

        entitySpawnTool.setShown(false);
        entitySpawnTool.setEnabled(false);

        itemSpawnEditorGuiComponent = new ItemSpawnEditorGuiComponent(0, 0, 500, 600);
        itemSpawnEditorGuiComponent.setShown(false);
        registerComponent(itemSpawnEditorGuiComponent);

        saveWorldGuiComponent = new SaveWorldGuiComponent(dg.getPreferredScreenWidth() / 2 - 250, dg.getPreferredScreenHeight() / 2 - 300, 500, 600, this);
        saveWorldGuiComponent.setShown(false);

        registerComponent(saveWorldGuiComponent);

        buttonMenu = new GuiImageBasedButton(20, height - 90, 80, 60, menuButtonTexture);
        int buttonHeight = 100;
        menuButtonContinue = new GuiTextBasedButton(width / 2 - width / 6, (int) (height / 2 + buttonHeight * 1.5f), width / 3, buttonHeight, "Continue!", true);
        menuButtonTestWorld = new GuiTextBasedButton(width / 2 - width / 6, height / 2 + buttonHeight / 2, width / 3, buttonHeight, "Test World!", true);
        menuButtonSaveWorld = new GuiTextBasedButton(width / 2 - width / 6, height / 2 - buttonHeight / 2, width / 3, buttonHeight, "Save World!", true);
        menuButtonExitLevelEditor = new GuiTextBasedButton(width / 2 - width / 6, (int) (height / 2 - buttonHeight * 1.5f), width / 3, buttonHeight, "Exit!", true);

        registerComponent(buttonMenu);
        registerComponent(menuButtonContinue);
        registerComponent(menuButtonTestWorld);
        registerComponent(menuButtonSaveWorld);
        registerComponent(menuButtonExitLevelEditor);

        showMenu(false);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        editorCamera.update();
        spriteBatch.enableBlending();
        editorShapeRenderer.setProjectionMatrix(editorCamera.combined);
        editorSpriteBatch.setProjectionMatrix(editorCamera.combined);
        editorSpriteBatch.enableBlending();

        ShaderProgram shader = dg.getOutlineShaderProgram();

        shader.begin();
        shader.setUniformf("u_viewportInverse", new Vector2(1f / width, 1f / height));
        shader.setUniformf("u_offset", 20);
        shader.setUniformf("u_step", Math.min(1f, width / 70f));
        shader.end();

        MousePos mP = GuiUtils.unprojectMousePosition(editorCamera);

        for (int x = 0; x <= map.getWidth(); x++) {
            for (int y = 0; y <= map.getHeight(); y++) {
                Block block = map.getBlock(x, y);
                if (block != null) {
                    //noinspection unchecked
                    dg.getBlockRendererRegistry().getRenderer(block).renderBlockInEditor(block, editorShapeRenderer, editorSpriteBatch, camera, x, y, 1, 1, partialTicks);
                }
            }
        }

        for (EntitySpawn entitySpawn : map.getEntitySpawnList()) {
            entitySpawnRendererRegistry.getRenderer(entitySpawn.getClass()).
                    renderInEditor(entitySpawn, mP.x, mP.y, entitySpawn.getX(), entitySpawn.getY(), entitySpawn.getWidth(), entitySpawn.getHeight(), false, editorShapeRenderer, editorSpriteBatch, map, editorCamera, partialTicks);
        }

        if (currentTool == EditorTool.ENTITY_SPAWN) {
            EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(currentSelectedSpawnType.type, mP.x, mP.y);
            entitySpawnRendererRegistry.getRenderer(entitySpawnCreationRegistry.getClass(currentSelectedSpawnType.type)).
                    renderInEditor(spawn, mP.x, mP.y, mP.x - spawn.getWidth() / 2, mP.y - spawn.getHeight() / 2, spawn.getWidth(), spawn.getHeight(), true, editorShapeRenderer, editorSpriteBatch, map, editorCamera, partialTicks);
        }

        if (currentTool == EditorTool.GRAB_ENTITY_SPAWN && grabbedEntitySpawn != null) {
            entitySpawnRendererRegistry.getRenderer(grabbedEntitySpawn.getClass()).
                    renderInEditor(grabbedEntitySpawn, mP.x, mP.y, mP.x - grabbedEntitySpawn.getWidth() / 2, mP.y - grabbedEntitySpawn.getHeight() / 2, grabbedEntitySpawn.getWidth(), grabbedEntitySpawn.getHeight(), true, editorShapeRenderer, editorSpriteBatch, map, editorCamera, partialTicks);

        }

        editorShapeRenderer.begin();

        if (showGrid) drawGrid(editorShapeRenderer);

        editorShapeRenderer.end();

        shader.begin();
        shader.setUniformf("u_color", new Vector3(1, 0, 0f));
        shader.end();

        if (currentTool == EditorTool.DELETE_ENTITY_SPAWNS && deleteEntitySpawnToolSelecting) {
            editorShapeRenderer.begin();
            editorShapeRenderer.set(ShapeType.Line);
            editorShapeRenderer.setColor(Color.RED);
            editorShapeRenderer.rect(lastInEditorClickX, lastInEditorClickY, mP.x - lastInEditorClickX, mP.y - lastInEditorClickY);
            editorShapeRenderer.end();
            editorSpriteBatch.setShader(shader);
            for (EntitySpawn entitySpawn : map.getEntitySpawnList()) {
                if (entitySpawn.getCollision().collidesWith(new Collision2D(lastInEditorClickX, lastInEditorClickY, mP.x - lastInEditorClickX, mP.y - lastInEditorClickY))) {
                    entitySpawnRendererRegistry.getRenderer(entitySpawn.getClass()).renderInEditor(entitySpawn, mP.x, mP.y, entitySpawn.getX(), entitySpawn.getY(), entitySpawn.getWidth(), entitySpawn.getHeight(), false, editorShapeRenderer, editorSpriteBatch, map, editorCamera, partialTicks);
                }
            }
            editorSpriteBatch.setShader(null);
        }

        if (currentTool == EditorTool.SELECT_ENTITY_SPAWN && selectedEntitySpawn != null) {
            shader.begin();
            shader.setUniformf("u_color", new Vector3(0, 1, 1f));
            shader.end();
            editorSpriteBatch.setShader(shader);
            entitySpawnRendererRegistry.getRenderer(selectedEntitySpawn.getClass()).renderInEditor(selectedEntitySpawn, mP.x, mP.y, selectedEntitySpawn.getX(), selectedEntitySpawn.getY(), selectedEntitySpawn.getWidth(), selectedEntitySpawn.getHeight(), false, editorShapeRenderer, editorSpriteBatch, map, editorCamera, partialTicks);
            editorSpriteBatch.setShader(null);
        }

        shapeRenderer.begin();
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));

        shapeRenderer.rect(0, 0, width, 200);

        shapeRenderer.end();

        shader.begin();
        shader.setUniformf("u_color", new Vector3(1, 1, 0f));
        shader.end();

        if (currentTool == EditorTool.PLACE_BLOCK) {
            spriteBatch.setShader(shader);
            //noinspection unchecked
            dg.getBlockRendererRegistry().getRenderer(currentSelectedBlock).renderBlockInScrollTool(currentSelectedBlock, shapeRenderer, spriteBatch, camera, 50, 50, 100, 100, partialTicks);
            spriteBatch.setShader(null);
        }

        dg.getBlockRendererRegistry().getRenderer(currentSelectedBlock).renderBlockInScrollTool(currentSelectedBlock, shapeRenderer, spriteBatch, camera, 50, 50, 100, 100, partialTicks);

        currentSelectedSpawnType.render(camera, shapeRenderer, spriteBatch, 250, 50, 100, 100, true, partialTicks);

        if (currentTool == EditorTool.ENTITY_SPAWN) {
            spriteBatch.setShader(shader);
            float size = 1.0f;
            currentSelectedSpawnType.render(camera, shapeRenderer, spriteBatch, 250, 50, 100 * size, 100 * size, true, partialTicks);
            spriteBatch.setShader(null);
        }

        if (currentTool == EditorTool.SELECT_ENTITY_SPAWN) {
            spriteBatch.setShader(shader);
            spriteBatch.begin();
            spriteBatch.draw(cursorTexture, 450, 50, 100, 100);
            spriteBatch.end();
            spriteBatch.setShader(null);
        }

        spriteBatch.begin();
        spriteBatch.draw(cursorTexture, 450, 50, 100, 100);
        spriteBatch.end();

        if (currentTool == EditorTool.DELETE_ENTITY_SPAWNS) {
            spriteBatch.setShader(shader);
            spriteBatch.begin();
            spriteBatch.draw(trashCanTexture, 650, 50, 100, 100);
            spriteBatch.end();
            spriteBatch.setShader(null);
        }

        spriteBatch.begin();
        spriteBatch.draw(trashCanTexture, 650, 50, 100, 100);
        spriteBatch.end();

        if (currentTool == EditorTool.GRAB_ENTITY_SPAWN) {
            spriteBatch.setShader(shader);
            spriteBatch.begin();
            spriteBatch.draw(grabTexture, 850, 50, 100, 100);
            spriteBatch.end();
            spriteBatch.setShader(null);
        }

        spriteBatch.begin();
        spriteBatch.draw(grabTexture, 850, 50, 100, 100);
        spriteBatch.end();

        renderGuiComponents(mouseX, mouseY, camera, partialTicks);

        editorSpriteBatch.disableBlending();
        spriteBatch.disableBlending();

        if (waitingForTestServer || waitingForClientConnection) {
            shapeRenderer.begin();
            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.set(ShapeType.Filled);
            shapeRenderer.rect(width / 2 - width / 4, height / 2 - 100, width / 2, 200);
            shapeRenderer.end();

            spriteBatch.begin();
            RenderUtils.drawString("Connecting to Server", dg.defaultFont20, width / 2, height / 2, spriteBatch, Color.WHITE, true);
            spriteBatch.end();
        }

    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        if (!itemSpawnEditorGuiComponent.isActive()) {
            if (GuiUtils.isPointInRegion(50, 50, 100, 100, mouseX, mouseY)) {
                blockBuildTool.setEnabled(true);
                blockBuildTool.setShown(true);
            }
            if (!blockBuildTool.isHovered(mouseX, mouseY) && !GuiUtils.isPointInRegion(50, 50, 100, 100, mouseX, mouseY)) {
                blockBuildTool.setEnabled(false);
                blockBuildTool.setShown(false);
            }
            if (GuiUtils.isPointInRegion(250, 50, 100, 100, mouseX, mouseY)) {
                entitySpawnTool.setEnabled(true);
                entitySpawnTool.setShown(true);
            }
            if (!entitySpawnTool.isHovered(mouseX, mouseY) && !GuiUtils.isPointInRegion(250, 50, 100, 100, mouseX, mouseY)) {
                entitySpawnTool.setEnabled(false);
                entitySpawnTool.setShown(false);
            }
        }
        if (waitingForTestServer) {
            if (DuckGamesClient.getDuckGames().getDedicatedServer().isServerReadyForConnections()) {
                waitingForTestServer = false;
                waitingForClientConnection = true;
                dg.connectToServer(new InetSocketAddress("localhost", CommonCode.getDuckGamesServer().getPort()), HostingType.LAN);
            }
        }
        if (waitingForClientConnection) {
            if (dg.getClientConnection().isIdentificationSuccessful()) {
                waitingForClientConnection = false;
                dg.getDedicatedServer().getMainServerThread().launchGame(map, DuckGamesServer.GameMode.TEST_WORLD);
            }
        }
        super.updateGui(mouseX, mouseY);
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == buttonMenu) {
            showMenu(!menuShown);
        } else if (button == menuButtonContinue) {
            showMenu(false);
        } else if (button == menuButtonTestWorld) {
            showMenu(false);
            testMap();
        } else if (button == menuButtonSaveWorld) {
            showMenu(false);
            saveWorldGuiComponent.setShown(true);
        } else if (button == menuButtonExitLevelEditor) {
            showMenu(false);
            dg.showGui(new GuiMainMenu());
        }
    }

    @Override
    public void keyPushed(int keyCode, int mouseX, int mouseY) {
        if (saveWorldGuiComponent.isShown()) return;
        switch (keyCode) {
            case T:
                testMap();
                break;
            case G:
                showGrid = !showGrid;
                break;
            case S:
                saveWorldGuiComponent.setShown(true);
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode, int mouseX, int mouseY) {
    }

    @Override
    protected void pointerDown(int mouseX, int mouseY, int pointer, int button) {
        MousePos mP = GuiUtils.unprojectMousePosition(editorCamera);
        if (hoversComponent(mouseX, mouseY)) return;
        lastInEditorClickX = mP.x;
        lastInEditorClickY = mP.y;
        lastClickInEditor = clickInEditorScreen(mouseX, mouseY);
        if (!input.isKeyPressed(Keys.SPACE) && lastClickInEditor) {
            List<EntitySpawn> entitySpawnsInRegion = map.getEntitySpawnsInRegion(new Collision2D(mP.x, mP.y, 1, 1));
            switch (currentTool) {
                case PLACE_BLOCK:
                    changeBlock(currentSelectedBlock);
                    break;
                case ENTITY_SPAWN:
                    spawnEntitySpawn();
                    break;
                case DELETE_ENTITY_SPAWNS:
                    deleteEntitySpawnToolSelecting = lastClickInEditor;
                    break;
                case SELECT_ENTITY_SPAWN:
                    if (!entitySpawnsInRegion.isEmpty()) {
                        selectedEntitySpawn = entitySpawnsInRegion.get(0);
                        if (selectedEntitySpawn instanceof ItemSpawner) {
                            itemSpawnEditorGuiComponent.setShown(true);
                            itemSpawnEditorGuiComponent.setItemSpawner((ItemSpawner) selectedEntitySpawn);
                        }
                    } else {
                        selectedEntitySpawn = null;
                    }
                    break;
                case GRAB_ENTITY_SPAWN:
                    if (grabbedEntitySpawn == null) {
                        if (!entitySpawnsInRegion.isEmpty()) {
                            grabbedEntitySpawn = entitySpawnsInRegion.get(0);
                            map.removeEntitySpawn(grabbedEntitySpawn);
                        }
                    } else {
                        if (map.canPlaceEntitySpawnAtPosition(grabbedEntitySpawn, mP.x, mP.y)) {
                            grabbedEntitySpawn.place(mP.x, mP.y);
                            map.addEntitySpawn(grabbedEntitySpawn);
                            grabbedEntitySpawn = null;
                        }
                    }
                    break;
            }
        }
        if (GuiUtils.isPointInRegion(450, 50, 100, 100, mouseX, mouseY)) {
            currentTool = EditorTool.SELECT_ENTITY_SPAWN;
        }
        if (GuiUtils.isPointInRegion(650, 50, 100, 100, mouseX, mouseY)) {
            currentTool = EditorTool.DELETE_ENTITY_SPAWNS;
        }
        if (GuiUtils.isPointInRegion(850, 50, 100, 100, mouseX, mouseY)) {
            currentTool = EditorTool.GRAB_ENTITY_SPAWN;
        }
    }

    @Override
    protected void pointerUp(int mouseX, int mouseY, int pointer, int button) {
        if (currentTool == EditorTool.DELETE_ENTITY_SPAWNS && deleteEntitySpawnToolSelecting) {
            MousePos mP = GuiUtils.unprojectMousePosition(editorCamera);
            deleteEntitySpawnToolSelecting = false;
            map.removeEntitySpawns(map.getEntitySpawnsInRegion(new Collision2D(lastInEditorClickX, lastInEditorClickY, mP.x - lastInEditorClickX, mP.y - lastInEditorClickY)));
        }
    }

    @Override
    protected void pointerDragged(int mouseX, int mouseY, int prevMouseX, int prevMouseY, int pointer) {
        if ((!hoversComponent(mouseX, mouseY) || !blockBuildTool.isEnabled()) && input.isKeyPressed(SPACE) && !itemSpawnEditorGuiComponent.isActive()) {
            MousePos mousePos = GuiUtils.unprojectMousePosition(editorCamera);
            MousePos prevMousePos = GuiUtils.unprojectMousePosition(editorCamera, unscaleMouseX(prevMouseX), Gdx.graphics.getHeight() - unscaleMouseY(prevMouseY));
            editorCamera.position.add(prevMousePos.x - mousePos.x, prevMousePos.y - mousePos.y, 0);
        }
        if (itemSpawnEditorGuiComponent.isActive() || hoversComponent(mouseX, mouseY)) return;
        switch (currentTool) {
            case PLACE_BLOCK:
                changeBlock(currentSelectedBlock);
                break;
        }
    }

    @Override
    protected void pointerMoved(int mouseX, int mouseY, int prevMouseX, int prevMouseY) {
    }

    @Override
    protected void scrolledWheel(int mouseX, int mouseY, int amount) {
        editorCamera.zoom += amount / 80f;
        if (editorCamera.zoom <= 0.0135f) editorCamera.zoom = 0.0135f;
    }

    @Override
    public void exitGui() {
        super.exitGui();
    }

    @Override
    public void scrollToolCallback(GuiScrollTool tool, GuiScrollTool.ScrollToolEntry entry) {
        if (tool == blockBuildTool) {
            currentSelectedBlock = ((BlockScrollToolEntry) entry).block;
            currentTool = EditorTool.PLACE_BLOCK;
        } else if (tool == entitySpawnTool) {
            currentSelectedSpawnType = (EntitySpawnToolEntry) entry;
            currentTool = EditorTool.ENTITY_SPAWN;
        }
        tool.setShown(false);
        tool.setEnabled(false);
    }

    private void changeBlock(Block block) {
        if (input.isKeyPressed(SPACE) || blockBuildTool.isShown() || entitySpawnTool.isShown() || hoversComponent(input.getX(), input.getY()) || menuShown)
            return;
        MousePos mousePos = GuiUtils.unprojectMousePosition(editorCamera);
        if (map.collisionCollidesWithEntitySpawns(new Collision2D((int) mousePos.x, (int) mousePos.y, 1, 1))) return;
        if (!(mousePos.x >= 0 && mousePos.x <= map.getWidth() + 1 && mousePos.y >= 0 && mousePos.y <= map.getHeight() + 1))
            return;
        map.setBlock((int) (mousePos.x), (int) (mousePos.y), block);

    }

    private void spawnEntitySpawn() {
        if (blockBuildTool.isShown() || entitySpawnTool.isShown() || menuShown) return;
        MousePos mP = GuiUtils.unprojectMousePosition(editorCamera);
        EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(currentSelectedSpawnType.type, mP.x, mP.y);
        if (!map.canPlaceEntitySpawnAtPosition(spawn)) return;
        map.addEntitySpawn(spawn);
    }

    private boolean clickInEditorScreen(int mouseX, int mouseY) {
        return GuiUtils.isPointInRegion(0, toolBarHeight, width, height - toolBarHeight, mouseX, mouseY);
    }

    private void drawGrid(ShapeRenderer renderer) {
        renderer.setColor(Color.WHITE);
        for (int x = 0; x <= map.getWidth() + 1; x++) {
            for (float y = 0; y <= map.getHeight(); y++) {
                renderer.line(x, y + 0.1f, x, y + 0.4f);
                renderer.line(x, y + 0.6f, x, y + 0.9f);
            }
        }
        for (int y = 0; y <= map.getHeight() + 1; y++) {
            for (float x = 0; x <= map.getWidth(); x++) {
                renderer.line(x + 0.1f, y, x + 0.4f, y);
                renderer.line(x + 0.6f, y, x + 0.9f, y);
            }
        }
    }

    private void showMenu(boolean show) {
        menuShown = show;
        menuButtonContinue.setShown(show);
        menuButtonSaveWorld.setShown(show);
        menuButtonTestWorld.setShown(show);
        menuButtonExitLevelEditor.setShown(show);
    }

    private void testMap() {
        if (map.canBeConvertedToWorld()) {
            DuckGamesServer server = new DuckGamesServer();
            server.startServer(DuckGamesServer.ServerState.CONNECT_STATE, new HostingInfo(HostingType.LAN, 0));
            dg.setDedicatedServer(server);
            allowInput = false;
            waitingForTestServer = true;
        }
    }

    public EditableGameMap getMap() {
        return map;
    }

    private static class BlockScrollToolEntry implements GuiScrollTool.ScrollToolEntry {

        Block block;

        BlockScrollToolEntry(Block block) {
            this.block = block;
        }

        @Override
        public void render(OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float x, float y, float width, float height, boolean highlighted, float partialTicks) {
            int r = highlighted ? 10 : 20;
            //noinspection unchecked
            DuckGamesClient.getDuckGames().getBlockRendererRegistry().getRenderer(block)
                    .renderBlockInScrollTool(block, sR, sB, camera, x + r, y + r, width - r * 2, height - r * 2, partialTicks);
        }
    }

    private static class EntitySpawnToolEntry implements GuiScrollTool.ScrollToolEntry {

        EntitySpawnType type;

        EntitySpawnToolEntry(EntitySpawnType type) {
            this.type = type;
        }

        @Override
        public void render(OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float x, float y, float width, float height, boolean highlighted, float partialTicks) {
            EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(type, x, y);
            int r = highlighted ? 10 : 20;
            entitySpawnRendererRegistry.getRenderer(spawn.getClass()).renderInScrollTool(spawn, x + r, y + r, width - r * 2, height - r * 2, sR, sB, camera, partialTicks);
        }
    }

    private enum EditorTool {
        PLACE_BLOCK,
        ENTITY_SPAWN,
        DELETE_ENTITY_SPAWNS,
        SELECT_ENTITY_SPAWN,
        GRAB_ENTITY_SPAWN
    }
}
