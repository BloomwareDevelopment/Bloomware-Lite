package me.offeex.bloomware.api.util;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.client.Colors;
import me.offeex.bloomware.client.module.modules.client.Gui;
import me.offeex.bloomware.client.setting.settings.SettingColor;

public class ColorUtils {

    public static int withAlpha(ColorMutable color, int transparency) {
        return ((transparency & 0xFF) << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    public static int darken(ColorMutable color, int value) {
        return (color.getAlpha() << 24) | (Math.max(0, color.getRed() - value) << 16) | (Math.max(0, color.getGreen() - value) << 8) | Math.max(0, color.getBlue() - value);
    }

    public static ColorMutable getGuiColor() {
        return Bloomware.moduleManager.getModule(Gui.class).color.getColor();
    }

    public static ColorMutable getTextColor() {
        return Bloomware.moduleManager.getModule(Colors.class).textColor.getColor();
    }

    public static ColorMutable getSliderColor() {
        return Bloomware.moduleManager.getModule(Colors.class).sliderColor.getColor();
    }

    public static int modify(ColorMutable original, int redOverwrite, int greenOverwrite, int blueOverwrite, int alphaOverwrite) {
        return (((alphaOverwrite == -1 ? original.getAlpha() : alphaOverwrite) & 0xFF) << 24) |
                (((redOverwrite == -1 ? original.getRed() : redOverwrite) & 0xFF) << 16) |
                (((greenOverwrite == -1 ? original.getGreen() : greenOverwrite) & 0xFF) << 8) |
                (((blueOverwrite == -1 ? original.getBlue() : blueOverwrite) & 0xFF));
    }

    public static ColorMutable getHudColor() {
        return Bloomware.moduleManager.getModule(Colors.class).hudColor.getColor();
    }

    public static ColorMutable getColor(String name) {
        Colors module = Bloomware.moduleManager.getModule(Colors.class);
        return ((SettingColor) module.getSetting(name)).getColor();
    }
}
