package me.offeex.bloomware.client.gui.screen.overlay.musicplayer.component;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.overlay.Overlay;
import net.minecraft.client.util.math.MatrixStack;

public class StopMusicButton extends Component {
    private int x, y;

    @Override
    public void render(MatrixStack matrix) {
        this.x = 130;
        this.y = mc.getWindow().getScaledHeight() - 53;
        Overlay.drawTexture(Overlay.STOP_ICON, x, y, 20, 20, matrix);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        Bloomware.musicManager.stopPlaying();
    }

    private boolean isHovered(double x, double y) {
        return x > this.x && x < this.x + 20 && y > this.y && y < this.y + 20;
    }
}
