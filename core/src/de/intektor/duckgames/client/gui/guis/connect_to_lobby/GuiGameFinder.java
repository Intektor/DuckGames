package de.intektor.duckgames.client.gui.guis.connect_to_lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiFrame;
import de.intektor.duckgames.client.gui.components.GuiScrollBar;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.guis.lobby.GuiLobby;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.server.HostingType;
import de.intektor.duckgames.common.net.lan.ServerInfo;
import de.intektor.duckgames.common.net.lan.ThreadFindLanServers;

import java.util.*;

/**
 * @author Intektor
 */
public class GuiGameFinder extends GuiFrame {

    private GuiScrollBar scrollBar;
    private List<GuiTextBasedButton> hostingTypeSelectors = new ArrayList<GuiTextBasedButton>();

    private HostingType currentlySelected = HostingType.LAN;
    private Map<HostingType, List<ServerInfo>> gameMap = new HashMap<HostingType, List<ServerInfo>>();

    private List<GuiTextBasedButton> buttons = new ArrayList<GuiTextBasedButton>();

    private List<HostingType> types;

    private static Texture refreshArrows = new Texture("refresh_arrows.png");
    private float prevRefreshAngle;
    private boolean requestedRefresh;

    public GuiGameFinder(int x, int y, int width, int height) {
        super(x, y, width, height, "Game Finder");

        types = new ArrayList<HostingType>();
        for (HostingType hostingType : HostingType.values()) {
            if (hostingType == HostingType.BLUETOOTH && !CommonCode.networking.isBluetoothAvailable()) continue;
            types.add(hostingType);
        }

        int cX = 0;
        for (HostingType hostingType : types) {
            GuiTextBasedButton button = new GuiTextBasedButton(cX, height - topBarHeight - 50, width / types.size(), 50, hostingType.name(), true);
            hostingTypeSelectors.add(button);
            registerGuiComponent(button);
            cX += width / types.size();
        }

        scrollBar = new GuiScrollBar(width, 0, 20, height - topBarHeight - 100, GuiScrollBar.Direction.VERTICAL, 0, height - topBarHeight);

        gameMap.put(HostingType.LAN, new ArrayList<ServerInfo>());
        gameMap.put(HostingType.INTERNET, new ArrayList<ServerInfo>());
        gameMap.put(HostingType.BLUETOOTH, new ArrayList<ServerInfo>());

        setSelected(HostingType.LAN);
        registerGuiComponent(scrollBar);
    }


    @Override
    public void setShown(boolean shown) {
        super.setShown(shown);
        if (shown)
            setSelected(currentlySelected);
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
        if (requestedRefresh) {
            prevRefreshAngle += 10f;
            prevRefreshAngle %= 360;
            if (currentlySelected == HostingType.LAN) {
                GuiConnectToLobby currentGui = (GuiConnectToLobby) dg.getCurrentGui();
                ThreadFindLanServers lanServerFinder = currentGui.getLanServerFinder();
                List<ServerInfo> serverInfos = gameMap.get(HostingType.LAN);
                if (lanServerFinder != null && lanServerFinder.getFoundServers() != null) {
                    Collection<ThreadFindLanServers.ServerEntry> values = lanServerFinder.getFoundServers().values();
                    for (ThreadFindLanServers.ServerEntry value : values) {
                        serverInfos.add(value.getInfo());
                    }
                    for (ServerInfo serverInfo : serverInfos) {
                        GuiTextBasedButton b = new RenderGuiTextBasedButton(0, 0, width, 50, "", true);
                        registerGuiComponent(b);
                        buttons.add(b);
                    }
                    requestedRefresh = false;
                }
            }
        }
    }

    @Override
    protected void drawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {
        sR.begin();
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.setColor(Color.GRAY);
        sR.rect(drawX, drawY, width, height - topBarHeight);

        sR.set(ShapeRenderer.ShapeType.Line);
        sR.setColor(Color.ORANGE);

        float top = drawY + height - topBarHeight - 51;
        float bottom = drawY + height - topBarHeight - 99;

        sR.line(drawX, top, drawX + width, top);

        sR.line(drawX + width / 3, top, drawX + width / 3, bottom);
        sR.line(drawX + width / 3 + width / 4, top, drawX + width / 3 + width / 4, bottom);
        sR.line(drawX + width / 3 + width / 2, top, drawX + width / 3 + width / 2, bottom);

        sR.line(drawX, bottom, drawX + width, bottom);

        sR.end();

        sB.begin();
        float ipX = drawX + width / 6;
        RenderUtils.drawString("IP:", dg.defaultFont28, ipX, drawY + height - topBarHeight - 75, sB, Color.ORANGE, true);
        float gamemodeX = drawX + width / 3 + width / 8;
        RenderUtils.drawString("Gamemode:", dg.defaultFont28, gamemodeX, drawY + height - topBarHeight - 75, sB, Color.ORANGE, true);
        float stateX = drawX + width / 3 + width / 4 + width / 8;
        RenderUtils.drawString("State:", dg.defaultFont28, stateX, drawY + height - topBarHeight - 75, sB, Color.ORANGE, true);
        int l = width - width / 3 - width / 4 - width / 4;
        float playersX = drawX + width / 3 + width / 4 + width / 4 + l / 2;
        RenderUtils.drawString("Players:", dg.defaultFont28, playersX, drawY + height - topBarHeight - 75, sB, Color.ORANGE, true);
        sB.end();

        int scrollAmt = (int) (scrollBar.getScrollPercent() * (buttons.size() * 50 - (height - topBarHeight - 100)));

        int y = this.height - 50 - topBarHeight - 100 + scrollAmt;
        for (GuiTextBasedButton button : buttons) {
            button.setPosition(0, y);
            button.setShown(y < height - topBarHeight - 100 && y >= -50);
            y -= 50;
        }

        if (requestedRefresh) {
            sB.begin();
            sB.enableBlending();
            RenderUtils.drawRotatedTexture(refreshArrows, sB, drawX + width / 2 - 50, drawY + height / 2 - 50, 100, prevRefreshAngle);
            sB.disableBlending();
            sB.end();
        }
    }

    @Override
    protected void postDrawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {
        float ipX = drawX + width / 6;
        float gamemodeX = drawX + width / 3 + width / 8;
        float stateX = drawX + width / 3 + width / 4 + width / 8;
        int l = width - width / 3 - width / 4 - width / 4;
        float playersX = drawX + width / 3 + width / 4 + width / 4 + l / 2;

        int scrollAmt = (int) (scrollBar.getScrollPercent() * (buttons.size() * 50 - (height - topBarHeight - 100)));

        int y = (int) (drawY + this.height - 50 - topBarHeight - 100 + scrollAmt + 25);

        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        sB.begin();
        for (ServerInfo serverInfo : gameMap.get(currentlySelected)) {
            RenderUtils.drawString(serverInfo.getIp() + ":" + serverInfo.getPort(), dg.defaultFont28, ipX, y, sB, Color.WHITE, true);
            RenderUtils.drawString(serverInfo.getGameMode().name() + "", dg.defaultFont28, gamemodeX, y, sB, Color.WHITE, true);
            RenderUtils.drawString(serverInfo.getState().name() + "", dg.defaultFont28, stateX, y, sB, Color.WHITE, true);
            RenderUtils.drawString(serverInfo.getPlayingPlayers() + "", dg.defaultFont28, playersX, y, sB, Color.WHITE, true);
            y -= 50;
        }
        sB.end();
        Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(GuiGameFinder.this.x), GuiUtils.unscaleScreenCoordY(GuiGameFinder.this.y),
                GuiUtils.unscaleScreenCoordX(GuiGameFinder.this.width), GuiUtils.unscaleScreenCoordY(GuiGameFinder.this.height - topBarHeight - 100));
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public Map<HostingType, List<ServerInfo>> getGameMap() {
        return gameMap;
    }

    private void setSelected(HostingType hostingType) {
        currentlySelected = hostingType;
        for (GuiTextBasedButton hostingTypeSelector : hostingTypeSelectors) {
            hostingTypeSelector.setTextColor(Color.WHITE);
        }
        hostingTypeSelectors.get(hostingType.ordinal()).setTextColor(Color.GREEN);

        for (GuiTextBasedButton button : buttons) {
            removeGuiComponent(button);
        }

        buttons.clear();

        List<ServerInfo> serverInfos = getGameMap().get(hostingType);
        serverInfos.clear();

        requestedRefresh = true;

        scrollBar.setAllWindowSize(serverInfos.size() * 50);
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button instanceof GuiTextBasedButton && hostingTypeSelectors.contains(button)) {
            int i = hostingTypeSelectors.indexOf(button);
            setSelected(types.get(i));
        } else if (buttons.contains(button)) {
            int i = buttons.indexOf(button);
            ServerInfo serverInfo = gameMap.get(currentlySelected).get(i);
            dg.connectToServer(serverInfo.getIp(), serverInfo.getPort(), currentlySelected);
            dg.showGui(new GuiLobby(false, null));
        }
    }

    private class RenderGuiTextBasedButton extends GuiTextBasedButton {

        public RenderGuiTextBasedButton(int x, int y, int width, int height, String text, boolean textCentered) {
            super(x, y, width, height, text, textCentered);
        }

        @Override
        protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
            Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(GuiGameFinder.this.x), GuiUtils.unscaleScreenCoordY(GuiGameFinder.this.y),
                    GuiUtils.unscaleScreenCoordX(GuiGameFinder.this.width), GuiUtils.unscaleScreenCoordY(GuiGameFinder.this.height - topBarHeight - 100));
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }
}
