package me.offeex.bloomware.client.setting.settings;

import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.SettingValueUpdateBus;

public class SettingColor extends Setting {
    public final SettingValueUpdateBus<ColorMutable> colorUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());

    private final ColorMutable color;

    public ColorMutable getColor() {
        return color;
    }

    private SettingColor(String name, Module module, String description, ColorMutable color) {
        super(name, module, description);
        this.color = color;
        this.color.onUpdate(() -> {
            this.colorUpdateBus.trigger(this.color, this.color);
        });
    }

    public static class Builder extends Setting.Builder {
        private ColorMutable color = new ColorMutable(255, 255, 255);

        public Builder(String name, String description) {
            super(name, description);
        }

        public Builder(String name) {
            super(name, "");
        }

        public Builder color(ColorMutable color) {
            this.color = color;
            return this;
        }

        @Override
        protected Setting make(Module module) {
            return new SettingColor(name, module, description, color);
        }
    }
}