package me.offeex.bloomware.client.gui.screen.overlay.musicplayer.component;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.overlay.Overlay;
import net.minecraft.client.util.math.MatrixStack;

public class PlaybackButton extends Component {
    private int x, y;

    @Override
    public void render(MatrixStack matrix) {
        this.x = 160;
        this.y = mc.getWindow().getScaledHeight() - 53;
        switch (Bloomware.musicManager.getMode()) {
            case NEXT -> Overlay.drawTexture(Overlay.REPEAT_PLAYBACK_ICON, x, y, 20, 20, matrix);
            case RANDOM -> Overlay.drawTexture(Overlay.SELECT_RANDOM_ICON, x, y, 20, 20, matrix);
            case REPEAT -> Overlay.drawTexture(Overlay.REPEAT_SONG_ICON, x, y, 20, 20, matrix);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int key) {
        if (isHovered(mouseX, mouseY)) {
            int index = Bloomware.musicManager.getMode().getId();
            Bloomware.musicManager.setMode(index < 2 ? ++index : 0);
        }
    }

    private boolean isHovered(double x, double y) {
        return x > this.x && x < this.x + 20 && y > this.y && y < this.y + 20;
    }
}
