package me.offeex.bloomware.client.setting.settings;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.SettingValueUpdateBus;

import java.util.ArrayList;
import java.util.List;

public class SettingGroup extends Setting {
    public final SettingValueUpdateBus<Boolean> toggledUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());

    private boolean toggled;
    private final boolean toggleable;
    private final List<Setting> settings = new ArrayList<>();

    public boolean isToggled() {
        return toggled;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setToggled(boolean toggled) {
        boolean oldValue = this.toggled;
        this.toggled = toggled;
        this.toggledUpdateBus.trigger(oldValue, this.toggled);
    }

    private SettingGroup(String name, Module module, String description, boolean toggled, boolean toggleable) {
        super(name, module, description);
        this.toggled = toggled;
        this.toggleable = toggleable;
    }

    public static class Builder extends Setting.Builder {
        private boolean toggled = false;
        private boolean toggleable = false;

        public Builder(String name, String description) {
            super(name, description);
        }

        public Builder(String name) {
            super(name, "");
        }

        public Builder toggled(boolean toggled) {
            this.toggled = toggled;
            return this;
        }

        public Builder toggleable(boolean toggleable) {
            this.toggleable = toggleable;
            return this;
        }

        @Override
        protected Setting make(Module module) {
            return new SettingGroup(name, module, description, toggled, toggleable);
        }
    }
}