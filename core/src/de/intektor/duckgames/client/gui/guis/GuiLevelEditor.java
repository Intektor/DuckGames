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
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiScrollTool;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.gui.util.MousePos;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.editor.EditableGameMap;
import de.intektor.duckgames.editor.EntitySpawn;
import de.intektor.duckgames.editor.EntitySpawn.EntitySpawnType;
import de.intektor.duckgames.editor.EntitySpawnCreationRegistry;
import de.intektor.duckgames.editor.spawns.MeleeSpawn;
import de.intektor.duckgames.editor.spawns.PlayerSpawn;
import de.intektor.duckgames.editor.spawns.renderer.EntitySpawnRendererRegistry;
import de.intektor.duckgames.editor.spawns.renderer.MeleeSpawnRenderer;
import de.intektor.duckgames.editor.spawns.renderer.PlayerSpawnRenderer;

import java.util.List;

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

    private float lastInEditorClickX;
    private float lastInEditorClickY;
    private boolean lastClickInEditor;

    private boolean deleteEntitySpawnToolSelecting;

    private final int toolBarHeight = 200;

    private EntitySpawn selectedEntitySpawn;

    private static EntitySpawnCreationRegistry entitySpawnCreationRegistry;
    private static EntitySpawnRendererRegistry entitySpawnRendererRegistry;

    private static Texture trashCanTexture = new Texture("assets/level_editor/trash_can.png");
    private static Texture cursorTexture = new Texture("assets/level_editor/cursor.png");

    static {
        entitySpawnCreationRegistry = new EntitySpawnCreationRegistry();
        entitySpawnCreationRegistry.register(EntitySpawnType.PLAYER_SPAWN, PlayerSpawn.class);
        entitySpawnCreationRegistry.register(EntitySpawnType.ITEM_SPAWN, MeleeSpawn.class);

        entitySpawnRendererRegistry = new EntitySpawnRendererRegistry();
        entitySpawnRendererRegistry.register(PlayerSpawn.class, new PlayerSpawnRenderer());
        entitySpawnRendererRegistry.register(MeleeSpawn.class, new MeleeSpawnRenderer());
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
        for (Block block : SharedGameRegistries.gameRegistry.getAllRegisteredBlocks()) {
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
            entitySpawnRendererRegistry.getRenderer(entitySpawn.getClass()).renderInEditor(entitySpawn, mP.x, mP.y, entitySpawn.getX(), entitySpawn.getY(), entitySpawn.getWidth(), entitySpawn.getHeight(), false, editorShapeRenderer, editorSpriteBatch);
        }

        if (currentTool == EditorTool.ENTITY_SPAWN) {
            EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(currentSelectedSpawnType.type, mP.x, mP.y);
            entitySpawnRendererRegistry.getRenderer(entitySpawnCreationRegistry.getClass(currentSelectedSpawnType.type)).renderInEditor(spawn, mP.x, mP.y, mP.x - spawn.getWidth() / 2, mP.y - spawn.getHeight() / 2, spawn.getWidth(), spawn.getHeight(), true, editorShapeRenderer, editorSpriteBatch);
        }
        editorShapeRenderer.begin();

        drawGrid(editorShapeRenderer);

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
                    entitySpawnRendererRegistry.getRenderer(entitySpawn.getClass()).renderInEditor(entitySpawn, mP.x, mP.y, entitySpawn.getX(), entitySpawn.getY(), entitySpawn.getWidth(), entitySpawn.getHeight(), false, editorShapeRenderer, editorSpriteBatch);
                }
            }
            editorSpriteBatch.setShader(null);
        }

        shapeRenderer.begin();
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.setColor(new Color(0x44E3FF));

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

        renderGuiComponents(mouseX, mouseY, camera, partialTicks);

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
            if (dg.getClientConnection().isIdentificationSuccessful()) {
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
        MousePos mP = GuiUtils.unprojectMousePosition(editorCamera);
        lastInEditorClickX = mP.x;
        lastInEditorClickY = mP.y;
        lastClickInEditor = clickInEditorScreen(mouseX, mouseY);
        if (!input.isKeyPressed(Keys.SPACE)) {
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
                    List<EntitySpawn> entitySpawnsInRegion = map.getEntitySpawnsInRegion(new Collision2D(mP.x, mP.y, 1, 1));
                    if (!entitySpawnsInRegion.isEmpty()) {
                        selectedEntitySpawn = entitySpawnsInRegion.get(0);
                    } else {
                        selectedEntitySpawn = null;
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
        if ((!hoversComponent(mouseX, mouseY) || !blockBuildTool.isEnabled()) && input.isKeyPressed(SPACE)) {
            MousePos mousePos = GuiUtils.unprojectMousePosition(editorCamera);
            MousePos prevMousePos = GuiUtils.unprojectMousePosition(editorCamera, unscaleMouseX(prevMouseX), Gdx.graphics.getHeight() - unscaleMouseY(prevMouseY));
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
        if (input.isKeyPressed(SPACE) || blockBuildTool.isShown() || entitySpawnTool.isShown()) return;
        MousePos mousePos = GuiUtils.unprojectMousePosition(editorCamera);
        if (map.collisionCollidesWithEntitySpawns(new Collision2D((int) mousePos.x, (int) mousePos.y, 1, 1))) return;
        if (!(mousePos.x >= 0 && mousePos.x <= map.getWidth() + 1 && mousePos.y >= 0 && mousePos.y <= map.getHeight() + 1))
            return;
        map.setBlock((int) (mousePos.x), (int) (mousePos.y), block);

    }

    private void spawnEntitySpawn() {
        if (blockBuildTool.isShown() || entitySpawnTool.isShown()) return;
        MousePos mP = GuiUtils.unprojectMousePosition(editorCamera);
        EntitySpawn spawn = entitySpawnCreationRegistry.createSpawn(currentSelectedSpawnType.type, mP.x, mP.y);
        if (!map.isCollisionInWorldBounds(spawn.getCollision())) return;
        if (map.doesEntitySpawnCollideWithSpawns(spawn, map.getEntitySpawnList())) return;
        if (map.collisionCollidesWithBlocks(spawn.getCollision())) return;
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
            entitySpawnRendererRegistry.getRenderer(spawn.getClass()).renderInScrollTool(spawn, x + r, y + r, width - r * 2, height - r * 2, sR, sB);
        }
    }

    private enum EditorTool {
        PLACE_BLOCK,
        ENTITY_SPAWN,
        DELETE_ENTITY_SPAWNS,
        SELECT_ENTITY_SPAWN
    }
}
