package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventSlowDown;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;

@Module.Register(name = "NoSlow", description = "Allows you to go faster in different situations", category = Module.Category.MOTION)
public class NoSlow extends Module {
    public final SettingBool items = new SettingBool.Builder("Items").setup(this);
    public final SettingBool soulSand = new SettingBool.Builder("SoulSand").setup(this);
    public final SettingBool webs = new SettingBool.Builder("Web").setup(this);
    public final SettingBool slimeBlock = new SettingBool.Builder("SlimeBlock").setup(this);
    public final SettingBool berryBush = new SettingBool.Builder("BerryBush").setup(this);
    public final SettingBool ladders = new SettingBool.Builder("Ladders").setup(this);
    public final SettingBool sneak = new SettingBool.Builder("Sneak").setup(this);

    @Subscribe
    private void onShouldSlowDown(EventSlowDown e) {
        e.setCancelled(sneak.getValue());
    }
}
