package me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class GroupButton extends SettingButton {
    private final SettingGroup setting = (SettingGroup) super.setting;
    private final ArrayList<SettingButton> settingButtons = new ArrayList<>();
    private boolean open;

    public GroupButton(final SettingGroup setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
        int settingY = this.offsetY + COMPONENT_HEIGHT;
        for (Setting s : setting.getSettings()) {
            switch (s.getType()) {
                case "SettingGroup" -> settingButtons.add(new GroupButton((SettingGroup) s, button, settingY));
                case "SettingEnum" -> settingButtons.add(new ModeButton((SettingEnum) s, button, settingY));
                case "SettingBool" -> settingButtons.add(new BooleanButton((SettingBool) s, button, settingY));
                case "SettingNumber" -> settingButtons.add(new SliderButton((SettingNumber) s, button, settingY));
                case "SettingColor" -> settingButtons.add(new ColorButton((SettingColor) s, button, settingY));
            }
        }
    }

    @Override
    public int getHeight() {
        return (open ? settingButtons.stream().map(Component::getHeight).reduce(0, Integer::sum) : 0) + COMPONENT_HEIGHT;
    }

    @Override
    public void setOffsetY(final int offsetY) {
        this.offsetY = offsetY;
        int settingY = this.offsetY + COMPONENT_HEIGHT;
        for (Component c : settingButtons) {
            c.setOffsetY(settingY);
            settingY += c.getHeight();
        }
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (isHovered(mouseX, mouseY)) {
            if (button == 0 && setting.isToggleable()) setting.setToggled(!setting.isToggled());
            if (button == 1) {
                open = !open;
                this.button.frame.update();
            }
        }

        if (open && this.button.isOpen()) settingButtons.forEach(c -> c.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (open && this.button.isOpen()) settingButtons.forEach(c -> c.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        super.updateComponent(mouseX, mouseY);
        if (open && this.button.isOpen()) settingButtons.forEach(component -> component.updateComponent(mouseX, mouseY));
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        if (setting.isToggleable())
            Bloomware.Font.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY, this.setting.isToggled() ? ColorUtils.getTextColor() : ColorMutable.WHITE);
        else
            Bloomware.Font.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY, ColorMutable.WHITE);

        if (!open)
            Bloomware.Font.drawString(matrix, "...", button.frame.getX() + button.frame.getWidth() - 2 - Bloomware.Font.getStringWidth("..."), button.frame.getY() + offsetY, ColorMutable.WHITE);
        else
            Bloomware.Font.drawString(matrix, "+", button.frame.getX() + button.frame.getWidth() - 2 - Bloomware.Font.getStringWidth("+"), button.frame.getY() + offsetY, ColorMutable.WHITE);

        if (open && this.button.isOpen()) settingButtons.forEach(c -> c.render(matrix));
    }
}
