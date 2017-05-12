package de.intektor.duckgames.client.gui.guis.play_state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.chat.IChatMessage;
import de.intektor.duckgames.util.TickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class GuiComponentMessagePreview extends GuiComponent {

    private List<MessagePrevInfo> list = new ArrayList<MessagePrevInfo>(5);

    public GuiComponentMessagePreview(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        sB.begin();
        sB.enableBlending();
        float y = drawY + height;
        for (MessagePrevInfo info : list) {
            Color color = info.message.getMessageColor().cpy();
            long l = dg.getTotalTickCount() - TickUtils.getTicksPerSecond() * 4;
            if (l >= info.tickAdded) {
                color.a = 1 - (float) (l - info.tickAdded) / TickUtils.getTicksPerSecond();
            }
            RenderUtils.drawString(info.message.getMessage(), dg.defaultFont28, drawX, y, sB, color);
            y -= dg.defaultFont28.getLineHeight();
        }
        sB.disableBlending();
        sB.end();
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
        List<MessagePrevInfo> removal = new ArrayList<MessagePrevInfo>(5);
        for (MessagePrevInfo info : list) {
            if (dg.getTotalTickCount() - TickUtils.getTicksPerSecond() * 5 >= info.tickAdded) {
                removal.add(info);
            }
        }
        list.removeAll(removal);
    }

    public void addMessage(IChatMessage message) {
        MessagePrevInfo info = new MessagePrevInfo(dg.getTotalTickCount(), message);
        if (list.size() == 5) {
            list.remove(0);
        }
        list.add(info);
    }

    private static class MessagePrevInfo {

        public MessagePrevInfo(long tickAdded, IChatMessage message) {
            this.tickAdded = tickAdded;
            this.message = message;
        }

        private long tickAdded;
        private IChatMessage message;
    }
}
