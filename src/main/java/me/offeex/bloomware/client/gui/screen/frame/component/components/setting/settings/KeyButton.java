package me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class KeyButton extends SettingButton {
    private boolean binding;

    public KeyButton(ModuleButton button, int offsetY) {
        super(button, offsetY);
    }

    @Override
    public int getHeight() {
        return COMPONENT_HEIGHT;
    }

    @Override
    public void setOffsetY(final int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (isHovered(mouseX, mouseY) && button == 0 && this.button.isOpen()) binding = !binding;
    }

    @Override
    public void keyTyped(final int key) {
        if (this.binding) {
            if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) button.module.setKey(-1);
            else button.module.setKey(key);
            this.binding = false;
        }
    }

    private LiteralText getKeyString() {
        String[] key = String.valueOf(InputUtil.fromKeyCode(button.module.getKey(), -1)).split("\\.");
        return new LiteralText(key[key.length - 1].toUpperCase(Locale.ROOT));
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        Bloomware.Font.drawString(matrix, "Key", button.frame.getX() + offsetNested, button.frame.getY() + offsetY, ColorUtils.getTextColor());
        if (binding)
            Bloomware.Font.drawString(matrix, "...", button.frame.getX() + button.frame.getWidth() - 2 - Bloomware.Font.getStringWidth("..."), button.frame.getY() + offsetY, ColorUtils.getTextColor());
        else {
            Text keyString = switch (button.module.getKey()) {
                case 341 -> new LiteralText("CTRL");
                case 344 -> new LiteralText("RSHIFT");
                case 345 -> new LiteralText("RCTRL");
                case 346 -> new LiteralText("RALT");
                case 256, 0, -1 -> new LiteralText("NONE");
                default -> getKeyString();
            };
            Bloomware.Font.drawString(matrix, keyString.asString(), button.frame.getX() + button.frame.getWidth() - 2 - Bloomware.Font.getStringWidth(keyString.asString()), button.frame.getY() + offsetY, ColorMutable.WHITE);
        }
    }
}
