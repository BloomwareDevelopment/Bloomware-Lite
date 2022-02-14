package me.offeex.bloomware.client.gui.screen.mainmenu.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class MainMenuButton extends Component {
    private boolean isHovered, isPressed;
    private int x;
    private int y;
    private final int width;
    private final int height;
    private final Screen screen;
    private final Identifier icon;

    public MainMenuButton(int x, int y, int width, int height, Screen screen, Identifier icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.screen = screen;
        this.icon = icon;
    }

    @Override
    public void render(MatrixStack matrix) {
        RenderSystem.setShaderTexture(0, icon);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        DrawableHelper.drawTexture(matrix, x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
    }

    public boolean isHovered(final double x, final double y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            mc.setScreen(screen);
            isPressed = true;
        }
    }

    public void updateProperties(int x_, int y_) {
        this.x = x_;
        this.y = y_;
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        isPressed = false;
    }
}
