package me.offeex.bloomware.client.gui.screen.frame.component.components;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.gui.screen.frame.Frame;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings.*;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.client.Gui;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import java.util.ArrayList;

public class ModuleButton extends Component {
    public Module module;
    public Frame frame;
    private int offsetY;
    private boolean isHovered;
    private boolean isPressed;
    private final ArrayList<SettingButton> settingButtons;
    private boolean open;

    public ModuleButton(Module module, Frame frame, int offsetY) {
        this.module = module;
        this.frame = frame;
        this.offsetY = offsetY;
        this.settingButtons = new ArrayList<>();
        this.open = false;
        int settingY = this.offsetY + Component.COMPONENT_HEIGHT;
        Bloomware.settingManager.getSettings(module).forEach(s -> {
            if (!Bloomware.settingManager.isInGroup(module, s)) {
                switch (s.getType()) {
                    case "SettingEnum" -> settingButtons.add(new ModeButton((SettingEnum) s, this, settingY));
                    case "SettingBool" -> settingButtons.add(new BooleanButton((SettingBool) s, this, settingY));
                    case "SettingNumber" -> settingButtons.add(new SliderButton((SettingNumber) s, this, settingY));
                    case "SettingGroup" -> settingButtons.add(new GroupButton((SettingGroup) s, this, settingY));
                    case "SettingColor" -> settingButtons.add(new ColorButton((SettingColor) s, this, settingY));
                }
            }
        });
        settingButtons.add(new KeyButton(this, settingY));
    }

    public int getOffsetY() {
        return offsetY;
    }

    public ArrayList<SettingButton> getSettingButtons() {
        return settingButtons;
    }

    public boolean isOpen() {
        return open;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isActive(SettingButton sb) {
        return sb.getSetting() == null || sb instanceof KeyButton || sb.getSetting().isActive();
    }

    @Override
    public void setOffsetY(final int offsetY) {
        this.offsetY = offsetY;
        int settingY = this.offsetY + SettingButton.COMPONENT_HEIGHT;
        for (SettingButton sb : settingButtons) {
            if (!isActive(sb)) continue;
            sb.setOffsetY(settingY);
            settingY += sb.getHeight();
        }
    }

    @Override
    public int getHeight() {
        return (open ? settingButtons.stream().map(sb -> isActive(sb) ? sb.getHeight() : 0).reduce(0, Integer::sum) : 0) + Component.COMPONENT_HEIGHT;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        for (SettingButton sb : settingButtons) {
            if (!isActive(sb)) continue;
                sb.updateComponent(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        for (SettingButton sb : settingButtons) {
            if (!isActive(sb) || !open) continue;
            sb.mouseClicked(mouseX, mouseY, button);
        }
        if (isHovered(mouseX, mouseY) && button == 0) {
            module.toggle();
            isPressed = true;
        }
        if (isHovered(mouseX, mouseY) && button == 1) {
            open = !open;
            frame.update();
        }
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        for (SettingButton sb : settingButtons) {
            if (!isActive(sb) || !open) continue;
            sb.mouseReleased(mouseX, mouseY, button);
        }
        isPressed = false;
    }

    @Override
    public void keyTyped(final int key) {
        settingButtons.forEach(component -> component.keyTyped(key));
    }

    @Override
    public void render(MatrixStack matrix) {
        if (isPressed) DrawableHelper.fill(matrix, frame.getX(), frame.getY() + offsetY, frame.getX() + frame.getWidth(), frame.getY() + offsetY + Component.COMPONENT_HEIGHT, ColorMutable.BLACK.getRGB());
        else DrawableHelper.fill(matrix, frame.getX(), frame.getY() + offsetY, frame.getX() + frame.getWidth(), frame.getY() + offsetY + Component.COMPONENT_HEIGHT, isHovered ? ColorUtils.getGuiColor().getRGB() : 0x46000000);

        if (Bloomware.settingManager.getSettings(module).size() > 1) {
            DrawableHelper.fill(matrix, frame.getX() + 107, frame.getY() + offsetY + 2, frame.getX() + 110, frame.getY() + offsetY + 10, ColorUtils.getGuiColor().getRGB());
        }

//        module name
        if (module.isEnabled()) {
            switch (Bloomware.moduleManager.getModule(Gui.class).enabledStyle.getSelectedStr()) {
                case "Glow" -> {
                    RenderUtil.drawGlow(matrix, frame.getX() + 5, frame.getY() + offsetY, (int) (frame.getX() + Bloomware.Font.getStringWidth(module.getName())), frame.getY() + offsetY + Component.COMPONENT_HEIGHT, ColorUtils.withAlpha(ColorUtils.getTextColor(), 50));
                    Bloomware.Font.drawString(matrix, module.getName(), frame.getX() + 3, frame.getY() + offsetY, module.isEnabled() ? ColorUtils.getTextColor() : ColorMutable.WHITE);
                }
                case "Fill" -> {
                    DrawableHelper.fill(matrix, frame.getX(), frame.getY() + offsetY, frame.getX() + frame.getWidth(), frame.getY() + offsetY + COMPONENT_HEIGHT, ColorUtils.getGuiColor().getRGB());
                    Bloomware.Font.drawString(matrix, module.getName(), frame.getX() + 3, frame.getY() + offsetY, ColorMutable.WHITE);
                }
                case "None" -> Bloomware.Font.drawString(matrix, module.getName(), frame.getX() + 3, frame.getY() + offsetY, module.isEnabled() ? ColorUtils.getTextColor() : ColorMutable.WHITE);
            }
        } else Bloomware.Font.drawString(matrix, module.getName(), frame.getX() + 3, frame.getY() + offsetY, ColorMutable.WHITE);

        if (open) {
            for (SettingButton sb : settingButtons) {
                if (!isActive(sb)) continue;
                sb.render(matrix);
            }
        }

//        description
        if (isHovered) Bloomware.Font.drawString(matrix, module.getDescription(), mc.getWindow().getScaledWidth() / 2f - Bloomware.Font.getStringWidth(module.getDescription()) / 2f, mc.getWindow().getScaledHeight() - 100, ColorMutable.WHITE);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.frame.getX() && x < this.frame.getX() + this.frame.getWidth() && y > this.frame.getY() + this.offsetY && y < this.frame.getY() + Component.COMPONENT_HEIGHT + this.offsetY;
    }
}