package me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderButton extends SettingButton {
    private final SettingNumber setting = (SettingNumber) super.setting;
    private boolean dragging;
    private int renderWidth;
    private final ColorMutable gray = new ColorMutable(170, 170, 170);

    public SliderButton(SettingNumber setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (isHovered(mouseX, mouseY) && this.button.isOpen()) {
            if (button == 0) dragging = true;
            else if (button == 1) setting.setValue(setting.getDefaultValue());
        }
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        super.mouseReleased(mouseX, mouseY, button);
        dragging = false;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        super.updateComponent(mouseX, mouseY);
        /*
         * sliderWidth is hitbox of slider
         * diff is difference between prev and current pos of slider
         * renderWidth is width we actually see
         */
        float sliderWidth = button.frame.getWidth() - offsetNested - 5;
        double diff = Math.min(sliderWidth, Math.max(0, mouseX - x - offsetNested));
        double min = setting.getMin();
        double max = setting.getMax();
        renderWidth = (int) (sliderWidth * (setting.getValue() - min) / (max - min)) + offsetNested;
        if (dragging) {
            if (diff <= 0) setting.setValue(setting.getMin());
            else if (diff >= sliderWidth) setting.setValue(setting.getMax());
            else {
                double newValue = round(diff / sliderWidth * (max - min) + min);
                double precision = 1.0D / setting.getInc();
                setting.setValue(Math.round(Math.max(min, Math.min(max, newValue)) * precision) / precision);
            }
        }
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        DrawableHelper.fill(matrix, button.frame.getX() + offsetNested, button.frame.getY() + offsetY + 13, button.frame.getX() + renderWidth, button.frame.getY() + offsetY + COMPONENT_HEIGHT, isHovered ? ColorUtils.darken(ColorUtils.getSliderColor(), 70) : ColorUtils.getSliderColor().getRGB());
        Bloomware.Font.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY, isHovered ? gray : ColorMutable.WHITE);
        Bloomware.Font.drawString(matrix, String.valueOf(round(setting.getValue())),
                button.frame.getX() + button.frame.getWidth() - 2 - Bloomware.Font.getStringWidth(String.valueOf(round(setting.getValue()))),
                button.frame.getY() + offsetY,
                isHovered ? gray : ColorMutable.WHITE);
    }

    private double round(final double value) {
        String nums = String.valueOf(setting.getInc()).split("\\.")[1];
        if (nums == null) {
            throw new IllegalArgumentException();
        } else {
            int places = nums.length();
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
