package me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.util.math.MatrixStack;

public class BooleanButton extends SettingButton {
    private final SettingBool setting = (SettingBool) super.setting;

    public BooleanButton(SettingBool setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && super.button.isOpen()) {
            setting.setValue(!setting.getValue());
        }
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        Bloomware.Font.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY, setting.getValue() ? ColorUtils.getTextColor() : ColorMutable.WHITE);
    }
}
