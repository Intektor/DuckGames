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
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.client.rendering.block.BlockRendererRegistry;
import de.intektor.duckgames.client.rendering.entity.EntityRendererRegistry;
import de.intektor.duckgames.client.rendering.item.ItemRendererRegistry;
import de.intektor.duckgames.client.rendering.utils.FutureTextureRegistry;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.Items;
import de.intektor.duckgames.world.WorldClient;
import de.intektor.network.IPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
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

    public volatile WorldClient theWorld;
    public volatile EntityPlayer thePlayer;

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

        showGui(new GuiMainMenu());
    }

    @Override
    public void render() {
        super.render();
        camera.update();
        if (System.nanoTime() - lastTickTime >= 15625000D) {
            lastTickTime = System.nanoTime();
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
        FontUtils.splitString("ienieinnievv", defaultFont28, 5);
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
        partialTicks = (System.nanoTime() - lastTickTime) / (15625000f);
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.begin();
        RenderUtils.drawString("fps: " + Gdx.graphics.getFramesPerSecond(), defaultFont12, 0, Gdx.graphics.getHeight(), spriteBatch, Color.WHITE);
        RenderUtils.drawString("p-ticks: " + partialTicks, defaultFont12, 0, Gdx.graphics.getHeight() - 12, spriteBatch, Color.WHITE);
        spriteBatch.end();
        spriteBatch.dispose();
        if (currentGui != null) currentGui.render(Gdx.input.getX(), Gdx.input.getY(), camera, partialTicks);
    }

    @Override
    public void dispose() {
        disconnect();
        try {
            getDedicatedServer().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Gui getCurrentGui() {
        return currentGui;
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

    public void connectToServer(InetSocketAddress address) {
        connectToServer(address.getHostName(), address.getPort());
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
        CommonCode.packetHelper.sendPacket(packet, clientConnection.getClientSocket());
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
        CommonCode.setDuckGamesServer(dedicatedServer);
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

    public float getPartialTicks() {
        return partialTicks;
    }
}
