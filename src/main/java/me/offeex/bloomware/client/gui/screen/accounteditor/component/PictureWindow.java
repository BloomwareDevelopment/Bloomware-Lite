package me.offeex.bloomware.client.gui.screen.accounteditor.component;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.client.gui.screen.accounteditor.component.PictureComponent;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public abstract class PictureWindow {
    private final Identifier WINDOW_TEXTURE;
    private final int x, y, width, height;
    public final ArrayList<Component> components;

    public PictureWindow(ArrayList<Component> components, Identifier texture, int x, int y, int width, int height) {
        this.WINDOW_TEXTURE = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height, width, height);
        components.forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
    }

    public boolean isHovered(final double x, final double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }
}
