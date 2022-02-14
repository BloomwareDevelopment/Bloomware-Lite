package me.offeex.bloomware.client.gui.screen.accounteditor.component.components.altmanager;

import me.offeex.bloomware.api.alts.Alt;
import me.offeex.bloomware.client.gui.screen.accounteditor.AccountEditor;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.overlay.Overlay;
import net.minecraft.client.util.math.MatrixStack;

public class AltWidget extends Component {
    private final Alt alt;
    private int xOffset = 0, yOffset = 0;
    private boolean isHovered;

    public AltWidget(Alt alt) {
        this.alt = alt;
    }

    public Alt getAlt() {
        return this.alt;
    }

    @Override
    public void render(MatrixStack matrices) {
        Overlay.drawTexture(AccountEditor.ALT_FIELD, xOffset, yOffset, 105, 106, matrices);
        if (isHovered) Overlay.drawTexture(AccountEditor.ALT_SELECTED_OUTLINE, xOffset + 6, yOffset + 6, 93, 95, matrices);
        else Overlay.drawTexture(AccountEditor.ALT_OUTLINE, xOffset + 6,  yOffset + 6, 93, 95, matrices);
    }

    public int getXOffset() {
        return xOffset;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}
