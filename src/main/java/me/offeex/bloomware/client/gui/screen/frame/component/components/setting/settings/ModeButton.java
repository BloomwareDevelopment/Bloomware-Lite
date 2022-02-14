package me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import net.minecraft.client.util.math.MatrixStack;

public class ModeButton extends SettingButton {
    private final SettingEnum setting = (SettingEnum) super.setting;
    private int modeIndex;

    public ModeButton(final SettingEnum setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
        this.modeIndex = 0;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (isHovered(mouseX, mouseY) && this.button.isOpen()) {
            final int maxIndex = setting.getModes().length - 1;
            if (button == 0) {
                ++modeIndex;
                if (modeIndex > maxIndex) modeIndex = 0;
                setting.setSelected(modeIndex);
            }

            if (button == 1) {
                --modeIndex;
                if (modeIndex < 0) modeIndex = maxIndex;
                setting.setSelected(modeIndex);
            }
        }
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        Bloomware.Font.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY, ColorUtils.getTextColor());
        Bloomware.Font.drawString(matrix, setting.getSelectedStr(), button.frame.getX() + button.frame.getWidth() - 2 - Bloomware.Font.getStringWidth(setting.getSelectedStr()), button.frame.getY() + offsetY, ColorUtils.getTextColor());
    }
}
