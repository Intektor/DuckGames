package de.intektor.duckgames;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.intektor.duckgames.block.BlockRegistry;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.guis.GuiLevelEditor;
import de.intektor.duckgames.client.net.DuckGamesClientConnection;
import de.intektor.duckgames.client.renderer.block.BlockRendererRegistry;
import de.intektor.duckgames.client.renderer.entity.EntityRendererRegistry;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.editor.EditableGameMap;
import de.intektor.duckgames.entity.EntityPlayer;
import de.intektor.duckgames.world.WorldClient;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DuckGamesClient extends ApplicationAdapter {

    private static DuckGamesClient dg;

    private Queue<Runnable> scheduledTasks = new LinkedBlockingQueue<Runnable>();

    private long lastTickTime;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final int preferredScreenWidth = 1920;
    private final int preferredScreenHeight = 1080;

    private ShapeRenderer defaultShapeRenderer;
    private SpriteBatch defaultSpriteBatch;
    private ModelBatch defaultModelBatch;

    private Gui currentGui;

    private BlockRendererRegistry blockRendererRegistry;
    private BlockRegistry blockRegistry;

    private EntityRendererRegistry entityRendererRegistry;

    private DuckGamesClientConnection clientConnection;

    private DuckGamesServer dedicatedServer;

    public WorldClient theWorld;
    public EntityPlayer thePlayer;

    @Override
    public void create() {
        dg = this;
        camera = new OrthographicCamera(preferredScreenWidth, preferredScreenHeight);
        viewport = new FillViewport(preferredScreenWidth, preferredScreenHeight, camera);
        viewport.apply(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        defaultShapeRenderer = new ShapeRenderer();
        defaultShapeRenderer.setAutoShapeType(true);
        defaultShapeRenderer.setProjectionMatrix(camera.combined);
        defaultSpriteBatch = new SpriteBatch();
        defaultSpriteBatch.setProjectionMatrix(camera.combined);
        defaultModelBatch = new ModelBatch();

        blockRendererRegistry = new BlockRendererRegistry();
        blockRegistry = new BlockRegistry();

        Blocks.initUniversal();
        Blocks.initClient();

        entityRendererRegistry = new EntityRendererRegistry();
        entityRendererRegistry.initDefaultEntities();

        showGui(new GuiLevelEditor(new EditableGameMap(40, 20)));
    }

    @Override
    public void render() {
        if (System.currentTimeMillis() - 15.625D >= lastTickTime) {
            lastTickTime = System.currentTimeMillis();
            updateGame();
        }
        camera.update();
        renderGame();
    }

    /**
     * The update method of the game: Called 64 times per second
     */
    private void updateGame() {
        Runnable r;
        while ((r = scheduledTasks.poll()) != null) {
            r.run();
        }
        if (currentGui != null) currentGui.update(Gdx.input.getX(), Gdx.input.getY());
    }

    /**
     * The render method of the game: Called as often as could
     */
    private void renderGame() {
        //Clear the last frame
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        defaultShapeRenderer.setProjectionMatrix(camera.combined);
        defaultSpriteBatch.setProjectionMatrix(camera.combined);
        if (currentGui != null) currentGui.render(Gdx.input.getX(), Gdx.input.getY(), camera);
    }

    @Override
    public void dispose() {

    }

    public void addScheduledTask(Runnable task) {
        scheduledTasks.offer(task);
    }

    public static DuckGamesClient getDuckGames() {
        return dg;
    }

    public int getPreferredScreenHeight() {
        return preferredScreenHeight;
    }

    public int getPreferredScreenWidth() {
        return preferredScreenWidth;
    }

    public ModelBatch getDefaultModelBatch() {
        return defaultModelBatch;
    }

    public ShapeRenderer getDefaultShapeRenderer() {
        return defaultShapeRenderer;
    }

    public SpriteBatch getDefaultSpriteBatch() {
        return defaultSpriteBatch;
    }

    public void showGui(Gui gui) {
        if (currentGui != null) currentGui.exitGui();
        currentGui = gui;
        currentGui.enterGui();
    }

    public OrthographicCamera getDefaultCamera() {
        return camera;
    }

    public BlockRendererRegistry getBlockRendererRegistry() {
        return blockRendererRegistry;
    }

    public BlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    public DuckGamesClientConnection getClientConnection() {
        return clientConnection;
    }

    public void connectToServer(String ip) {
        connectToServer(ip, 19473);
    }

    public void connectToServer(String ip, int port) {
        try {
            if (this.clientConnection != null) this.clientConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.clientConnection = new DuckGamesClientConnection();
        this.clientConnection.connect(ip, port);
    }

    public void disconnect() {
        try {
            if (this.clientConnection != null) this.clientConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDedicatedServer(DuckGamesServer dedicatedServer) {
        try {
            if (this.dedicatedServer != null) this.dedicatedServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dedicatedServer = dedicatedServer;
    }

    public DuckGamesServer getDedicatedServer() {
        return dedicatedServer;
    }

    public EntityRendererRegistry getEntityRendererRegistry() {
        return entityRendererRegistry;
    }
}
