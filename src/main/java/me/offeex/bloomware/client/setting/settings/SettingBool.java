package me.offeex.bloomware.client.setting.settings;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.SettingValueUpdateBus;

public class SettingBool extends Setting {
    public final SettingValueUpdateBus<Boolean> valueUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());

    private boolean value;

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        boolean oldValue = this.value;
        this.value = value;
        this.valueUpdateBus.trigger(oldValue, this.value);
    }

    private SettingBool(String name, Module module, String description, boolean value) {
        super(name, module, description);
        this.value = value;
    }

    public static class Builder extends Setting.Builder {
        private boolean value = false;

        public Builder(String name, String description) {
            super(name, description);
        }

        public Builder(String name) {
            super(name, "");
        }

        public Builder value(boolean value) {
            this.value = value;
            return this;
        }

        @Override
        protected Setting make(Module module) {
            return new SettingBool(name, module, description, value);
        }
    }
}