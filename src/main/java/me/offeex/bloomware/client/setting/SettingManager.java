package me.offeex.bloomware.client.setting;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingManager {
//    TODO: Move settings to each module instance

    private final List<Setting> settings;

    public SettingManager() {
        this.settings = new ArrayList<>();
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public void add(Setting setting) {
        setting.setId(String.valueOf(getSettings(setting.getModule()).size()));
        this.settings.add(setting);
    }

    public List<Setting> getSettings(Module module) {
        return this.settings.stream().filter(s -> s.getModule().equals(module)).collect(Collectors.toList());
    }

    public Setting getGroup(Module module, Setting setting) {
        for (Setting s : Bloomware.settingManager.getSettings(module)) {
            if (!(s instanceof SettingGroup)) continue;
            return ((SettingGroup) s).getSettings().stream().filter(s1 -> s1 == setting).findFirst().orElse(null);
        }
        return null;
    }

    public boolean isInGroup(Module module, Setting s) {
        for (Setting setting : Bloomware.settingManager.getSettings(module)) {
            if (!(setting instanceof SettingGroup)) continue;
                for (Setting setting1 : ((SettingGroup) setting).getSettings()) if (setting1 == s) return true;
        }
        return false;
    }
}