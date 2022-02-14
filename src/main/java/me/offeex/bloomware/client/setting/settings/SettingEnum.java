package me.offeex.bloomware.client.setting.settings;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.SettingValueUpdateBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingEnum extends Setting {
    public final SettingValueUpdateBus<Integer> selectedUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());
    public final SettingValueUpdateBus<String> selectedStrUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());


    private int selected;
    private final String[] modes;
    private final List<Setting> settings = new ArrayList<>();

    public String[] getModes() {
        return modes;
    }

    public int getSelected() {
        return selected;
    }

    public String getSelectedStr() {
        return modes[selected];
    }
    
    public boolean is(String value) {
        return modes[selected].equals(value);
    }

    public void setSelected(int selected) {
        if (selected < 0 || selected > modes.length - 1) throw new IllegalArgumentException();
        int oldValue = this.selected;
        this.selected = selected;
        this.selectedUpdateBus.trigger(oldValue, this.selected);
        this.selectedStrUpdateBus.trigger(modes[oldValue], modes[this.selected]);
    }

    public void setSelected(String mode) {
        int tmp = Arrays.asList(modes).indexOf(mode);
        if (tmp < 0) throw new IllegalArgumentException();
        int oldValue = this.selected;
        this.selected = tmp;
        this.selectedUpdateBus.trigger(oldValue, this.selected);
        this.selectedStrUpdateBus.trigger(modes[oldValue], modes[this.selected]);
    }

    private SettingEnum(String name, Module module, String description, int selected, String[] modes) {
        super(name, module, description);
        this.modes = modes;
        this.selected = selected;
    }

    public static class Builder extends Setting.Builder {
        private String[] modes = {};
        private int selected = 0;

        public Builder(String name, String description) {
            super(name, description);
        }

        public Builder(String name) {
            super(name, "");
        }

        public Builder modes(String... modes) {
            this.modes = modes;
            return this;
        }

        public Builder selected(int selected) {
            this.selected = selected;
            return this;
        }

        public Builder selected(String mode) {
            int tmp = Arrays.asList(modes).indexOf(mode);
            if (tmp < 0) throw new IllegalArgumentException();
            this.selected = tmp;
            return this;
        }

        @Override
        protected Setting make(Module module) {
            return new SettingEnum(name, module, description, selected, modes);
        }
    }
}
