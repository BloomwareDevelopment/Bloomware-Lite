package me.offeex.bloomware.client.gui.screen.accounteditor.component;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public abstract class PictureComponent extends Component {
    private final Identifier TEXTURE;
    private final int x, y, width, height;

    public PictureComponent(Identifier texture, int x, int y, int width, int height) {
        this.TEXTURE = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0,width, height, width, height);
    }

    public boolean isHovered(double x, double y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }
}
