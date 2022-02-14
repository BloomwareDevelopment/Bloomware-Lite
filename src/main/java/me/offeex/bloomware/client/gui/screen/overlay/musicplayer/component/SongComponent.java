package me.offeex.bloomware.client.gui.screen.overlay.musicplayer.component;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.overlay.Overlay;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class SongComponent extends Component {
    private final File track;
    private int offset;

    public SongComponent(File file) {
        this.track = file;
    }

    @Override
    public void render(MatrixStack matrix) {
        RenderSystem.setShaderTexture(0, Overlay.TRACK_ICON);
        RenderSystem.enableBlend();
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        DrawableHelper.drawTexture(matrix, 10, offset, 0, 0, 40, 40, 40, 40);
        Bloomware.Font.drawString(matrix, track.getName(),70, offset + 10, ColorMutable.WHITE);
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY)) {
            new Thread(() -> Bloomware.musicManager.play(track.getPath())).start();
        }
    }

    private boolean isHovered(double x, double y) {
        return x > 15 && x < mc.getWindow().getScaledWidth() - 185 && y > offset && y < offset + 40;
    }

    public void setOffset(int newOffset) {
        this.offset = newOffset;
    }

    public File getTrack() {
        return track;
    }
}
