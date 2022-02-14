package me.offeex.bloomware.client.gui.screen.frame.component.components.setting;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings.KeyButton;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public abstract class SettingButton extends Component {
    protected Setting setting = null;
    protected final ModuleButton button;
    protected boolean isHovered;
    protected int offsetY;
    protected int x;
    protected int y;

    public SettingButton(final Setting setting, ModuleButton button, int offsetY) {
        this.setting = setting;
        this.button = button;
        this.x = button.frame.getX() + button.frame.getWidth();
        this.y = button.frame.getY() + button.getOffsetY();
        this.offsetY = offsetY;
        setting.getUpdateBus().subscribe(() -> button.frame.update());
        if (!(this instanceof KeyButton))
            offsetNested += 3 * this.getNestingLevel();

    }

    public SettingButton(ModuleButton button, int offsetY) {
        this.button = button;
        this.x = button.frame.getX() + button.frame.getWidth();
        this.y = button.frame.getY() + button.getOffsetY();
        this.offsetY = offsetY;
    }

    public int getHeight() {
        return COMPONENT_HEIGHT;
    }

    public Setting getSetting() {
        return setting;
    }

    @Override
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {}

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {}

    @Override
    public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix, button.frame.getX(), button.frame.getY() + offsetY, button.frame.getX() + button.frame.getWidth(), button.frame.getY() + offsetY + COMPONENT_HEIGHT, isHovered ? 0x96000000 : 0x82000000);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + COMPONENT_HEIGHT;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        x = button.frame.getX();
        y = button.frame.getY() + this.offsetY;
    }

    public int getNestingLevel() {
        if (setting == null) return 0;
        return calcNestingLevel(0, setting);
    }

    private int calcNestingLevel(int baseLevel, Setting setting) {
        for (Setting s : Bloomware.settingManager.getSettings(button.module)) {
            if (s instanceof SettingGroup && ((SettingGroup)s).getSettings().contains(setting))
                return calcNestingLevel(baseLevel + 1, s);
        }
        return baseLevel;
    }
}
