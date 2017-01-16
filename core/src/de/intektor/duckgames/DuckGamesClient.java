package de.intektor.duckgames;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.intektor.duckgames.block.Blocks;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.guis.GuiMainMenu;
import de.intektor.duckgames.client.net.DuckGamesClientConnection;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.client.rendering.block.BlockRendererRegistry;
import de.intektor.duckgames.client.rendering.entity.EntityRendererRegistry;
import de.intektor.duckgames.client.rendering.item.ItemRendererRegistry;
import de.intektor.duckgames.client.rendering.utils.FutureTextureRegistry;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.Items;
import de.intektor.duckgames.world.WorldClient;
import de.intektor.network.IPacket;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DuckGamesClient extends ApplicationAdapter {

    private static DuckGamesClient dg;

    private Queue<Runnable> scheduledTasks = new LinkedBlockingQueue<Runnable>();

    private long lastTickTime;

    private OrthographicCamera camera;
    private Viewport viewport;

    public BitmapFont defaultFont12;
    public BitmapFont defaultFont16;
    public BitmapFont defaultFont20;
    public BitmapFont defaultFont28;
    public BitmapFont defaultFont54;
    public BitmapFont defaultFont72;

    private final int preferredScreenWidth = 1920;
    private final int preferredScreenHeight = 1080;

    private ShapeRenderer defaultShapeRenderer;
    private SpriteBatch defaultSpriteBatch;
    private ModelBatch defaultModelBatch;

    private ShaderProgram outlineShaderProgram;

    private Gui currentGui;

    private BlockRendererRegistry blockRendererRegistry;
    private EntityRendererRegistry entityRendererRegistry;
    private ItemRendererRegistry itemRendererRegistry;

    private DuckGamesClientConnection clientConnection;

    private DuckGamesServer dedicatedServer;

    public WorldClient theWorld;
    public EntityPlayer thePlayer;

    private float partialTicks;

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
        defaultSpriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        defaultModelBatch = new ModelBatch();

        blockRendererRegistry = new BlockRendererRegistry();
        itemRendererRegistry = new ItemRendererRegistry();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("assets/font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        defaultFont12 = gen.generateFont(parameter);
        parameter.size = 16;
        defaultFont16 = gen.generateFont(parameter);
        parameter.size = 20;
        defaultFont20 = gen.generateFont(parameter);
        parameter.size = 28;
        defaultFont28 = gen.generateFont(parameter);
        parameter.size = 54;
        defaultFont54 = gen.generateFont(parameter);
        parameter.size = 72;
        defaultFont72 = gen.generateFont(parameter);
        gen.dispose();

        Blocks.initClient();
        Items.initClient();

        outlineShaderProgram = new ShaderProgram(Gdx.files.internal("assets/shader/outline_shader_vertex.glsl"), Gdx.files.internal("assets/shader/outline_shader_fragment.glsl"));

        entityRendererRegistry = new EntityRendererRegistry();
        entityRendererRegistry.initDefaultEntities();

        FutureTextureRegistry.loadTextures();

//        showGui(new GuiLevelEditor(new EditableGameMap(80, 40)));
        showGui(new GuiMainMenu());
    }

    @Override
    public void render() {
        camera.update();
        if (System.currentTimeMillis() - lastTickTime >= 15.625D) {
            lastTickTime = System.currentTimeMillis();
            updateGame();
        }
        renderGame();
    }

    /**
     * The updateWorld method of the game: Called 64 times per second
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
        camera.update();
        defaultShapeRenderer.setProjectionMatrix(camera.combined);
        defaultSpriteBatch.setProjectionMatrix(camera.combined);
        partialTicks = (System.currentTimeMillis() - lastTickTime) / (15.625f);
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.begin();
        RenderUtils.drawString(Gdx.graphics.getFramesPerSecond() + "", defaultFont12, 10, getPreferredScreenHeight() / 2, spriteBatch, Color.WHITE);
        spriteBatch.end();
        spriteBatch.dispose();
        if (currentGui != null) currentGui.render(Gdx.input.getX(), Gdx.input.getY(), camera, partialTicks);
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

    public void sendPacketToServer(IPacket packet) {
        SharedGameRegistries.packetHelper.sendPacket(packet, clientConnection.getClientSocket());
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
        SharedGameRegistries.setDuckGamesServer(dedicatedServer);
    }

    public DuckGamesServer getDedicatedServer() {
        return dedicatedServer;
    }

    public EntityRendererRegistry getEntityRendererRegistry() {
        return entityRendererRegistry;
    }

    public ItemRendererRegistry getItemRendererRegistry() {
        return itemRendererRegistry;
    }

    public ShaderProgram getOutlineShaderProgram() {
        return outlineShaderProgram;
    }
}
