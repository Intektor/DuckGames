package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiScrollTool;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.gui.util.MousePos;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.editor.EditableGameMap;
import de.intektor.duckgames.editor.EntitySpawn;
import de.intektor.duckgames.editor.EntitySpawn.EntitySpawnType;
import de.intektor.duckgames.editor.EntitySpawnCreationRegistry;
import de.intektor.duckgames.editor.spawns.PlayerSpawn;
import de.intektor.duckgames.editor.spawns.renderer.EntitySpawnRendererRegistry;
import de.intektor.duckgames.editor.spawns.renderer.PlayerSpawnRenderer;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.T;

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

    private Block currentSelectedBlock;
    private EntitySpawnToolEntry currentSelectedSpawnType;

    private EditorTool currentTool;

    private boolean waitingForTestServer;
    private boolean waitingForClientConnection;

    private static EntitySpawnCreationRegistry entitySpawnCreationRegistry;
    private static EntitySpawnRendererRegistry entitySpawnRendererRegistry;

    static {
        entitySpawnCreationRegistry = new EntitySpawnCreationRegistry();
        entitySpawnCreationRegistry.register(EntitySpawnType.PLAYER_SPAWN, PlayerSpawn.class);

        entitySpawnRendererRegistry = new EntitySpawnRendererRegistry();
        entitySpawnRendererRegistry.register(PlayerSpawn.class, new PlayerSpawnRenderer());
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
        for (Block block : dg.getBlockRegistry().getAllRegisteredBlocks()) {
            blockBuildTool.addEntry(new BlockScrollToolEntry(block));
        }

        registerComponent(blockBuildTool);
        blockBuildTool.setShown(false);
        blockBuildTool.setEnabled(false);

        entitySpawnTool = new GuiScrollTool<EntitySpawnToolEntry>(250, 150, 500, 600, true, false, false, 100, 100, 7, this);
        currentSelectedSpawnType = new EntitySpawnToolEntry(EntitySpawnType.PLAYER_SPAWN);
        entitySpawnTool.addEntry(currentSelectedSpawnType);
        registerComponent(entitySpawnTool);
        entitySpawnTool.setShown(false);
        entitySpawnTool.setEnabled(false);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera) {
        MousePos mP = unprojectMousePosition();
        editorCamera.update();
        spriteBatch.enableBlending();
        editorShapeRenderer.setProjectionMatrix(editorCamera.combined);
        editorSpriteBatch.setProjectionMatrix(editorCamera.combined);
        editorSpriteBatch.enableBlending();

        for (int x = 0; x <= map.getWidth(); x++) {
            for (int y = 0; y <= map.getHeight(); y++) {
                Block block = map.getBlock(x, y);
                if (block != null) {
                    //noinspection unchecked
                    dg.getBlockRendererRegistry().getRenderer(block).renderBlockInEditor(block, editorShapeRenderer, editorSpriteBatch, camera, x, y, 1, 1);
                }
            }
        }

        for (EntitySpawn entitySpawn : map.getEntitySpawnList()) {
            entitySpawnRendererRegistry.getRenderer(entitySpawn.getClass()).render(entitySpawn, mP.x, mP.y, entitySpawn.getX(), entitySpawn.getY(), entitySpawn.getWidth(), entitySpawn.getHeight(), false, editorShapeRenderer, editorSpriteBatch);
        }


        if (currentTool == EditorTool.ENTITY_SPAWN) {
            EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(currentSelectedSpawnType.type, mP.x, mP.y);
            entitySpawnRendererRegistry.getRenderer(entitySpawnCreationRegistry.getClass(currentSelectedSpawnType.type)).render(spawn, mP.x, mP.y, mP.x - spawn.getWidth() / 2, mP.y - spawn.getHeight() / 2, spawn.getWidth(), spawn.getHeight(), true, editorShapeRenderer, editorSpriteBatch);
        }
        editorShapeRenderer.begin();

//        drawGrid(editorShapeRenderer);

        editorShapeRenderer.end();

        shapeRenderer.begin();
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.setColor(new Color(0x44E3FF));

        shapeRenderer.rect(0, 0, width, 200);

        shapeRenderer.end();
        //noinspection unchecked
        dg.getBlockRendererRegistry().getRenderer(currentSelectedBlock).renderBlockInScrollTool(currentSelectedBlock, shapeRenderer, spriteBatch, camera, 50, 50, 100, 100);

        currentSelectedSpawnType.render(camera, shapeRenderer, spriteBatch, 250, 50, 100, 100, true);

        renderGuiComponents(mouseX, mouseY, camera);

        editorSpriteBatch.disableBlending();
        spriteBatch.disableBlending();

        if (waitingForTestServer || waitingForClientConnection) {
            shapeRenderer.begin();

            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.set(ShapeType.Filled);
            shapeRenderer.rect(width / 2 - width / 4, height / 2 - 100, width / 2, 200);

            shapeRenderer.end();
        }
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
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
        if (waitingForTestServer) {
            if (DuckGamesClient.getDuckGames().getDedicatedServer().isServerReadyForConnections()) {
                waitingForTestServer = false;
                waitingForClientConnection = true;
                dg.connectToServer("localhost");
            }
        }
        if (waitingForClientConnection) {
            if (dg.getClientConnection().isConnected()) {
                waitingForClientConnection = false;
                dg.getDedicatedServer().getMainServerThread().launchGame(map);
            }
        }
    }

    @Override
    public void buttonCallback(GuiButton button) {

    }

    @Override
    public void keyPushed(int keyCode, int mouseX, int mouseY) {
        switch (keyCode) {
            case T:
                DuckGamesServer server = new DuckGamesServer();
                server.startServer();
                dg.setDedicatedServer(server);
                allowInput = false;
                waitingForTestServer = true;
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode, int mouseX, int mouseY) {
    }

    @Override
    protected void pointerDown(int mouseX, int mouseY, int pointer, int button) {
        switch (currentTool) {
            case PLACE_BLOCK:
                changeBlock(currentSelectedBlock);
                break;
            case ENTITY_SPAWN:
                spawnEntitySpawn();
                break;
        }
    }

    @Override
    protected void pointerUp(int mouseX, int mouseY, int pointer, int button) {
    }

    @Override
    protected void pointerDragged(int mouseX, int mouseY, int prevMouseX, int prevMouseY, int pointer) {
        if ((!hoversComponent(mouseX, mouseY) || !blockBuildTool.isEnabled()) && input.isKeyPressed(SPACE)) {
            MousePos mousePos = unprojectMousePosition();
            MousePos prevMousePos = unprojectMousePosition(unscaleMouseX(prevMouseX), Gdx.graphics.getHeight() - unscaleMouseY(prevMouseY));
            editorCamera.position.add(prevMousePos.x - mousePos.x, prevMousePos.y - mousePos.y, 0);
        }
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

    private MousePos unprojectMousePosition() {
        return unprojectMousePosition(input.getX(), input.getY());
    }

    private MousePos unprojectMousePosition(int mouseX, int mouseY) {
        Vector3 unproject = editorCamera.unproject(new Vector3(mouseX, mouseY, 0));
        return new MousePos(unproject.x, unproject.y);
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
    }

    private void changeBlock(Block block) {
        if (input.isKeyPressed(SPACE) || blockBuildTool.isShown()) return;
        MousePos mousePos = unprojectMousePosition();
        if (mousePos.x >= 0 && mousePos.x <= map.getWidth() + 1 && mousePos.y >= 0 && mousePos.y <= map.getHeight() + 1) {
            map.setBlock((int) (mousePos.x), (int) (mousePos.y), block);
        }
    }

    private void spawnEntitySpawn() {
        MousePos mP = unprojectMousePosition();
        EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(currentSelectedSpawnType.type, mP.x, mP.y);
        if (!map.isCollisionInWorldBounds(spawn.getCollision())) return;
        if (map.doesEntitySpawnCollideWithSpawns(spawn, map.getEntitySpawnList())) return;
        map.addEntitySpawn(spawn);
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


    private static class BlockScrollToolEntry implements GuiScrollTool.ScrollToolEntry {

        Block block;

        BlockScrollToolEntry(Block block) {
            this.block = block;
        }

        @Override
        public void render(OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float x, float y, float width, float height, boolean highlighted) {
            int r = highlighted ? 10 : 20;
            //noinspection unchecked
            DuckGamesClient.getDuckGames().getBlockRendererRegistry().getRenderer(block)
                    .renderBlockInScrollTool(block, sR, sB, camera, x + r, y + r, width - r * 2, height - r * 2);
        }
    }

    private static class EntitySpawnToolEntry implements GuiScrollTool.ScrollToolEntry {

        EntitySpawnType type;

        EntitySpawnToolEntry(EntitySpawnType type) {
            this.type = type;
        }

        @Override
        public void render(OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float x, float y, float width, float height, boolean highlighted) {
            sR.begin();
            sR.set(ShapeType.Filled);
            sR.setColor(Color.YELLOW);
            int r = highlighted ? 10 : 20;
            sR.rect(x + r, y + r, width - r * 2, height - r * 2);
            sR.end();
        }
    }

    private enum EditorTool {
        PLACE_BLOCK,
        ENTITY_SPAWN
    }
}
