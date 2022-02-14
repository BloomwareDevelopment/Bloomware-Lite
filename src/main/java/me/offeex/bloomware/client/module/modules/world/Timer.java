package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventBeginRenderTick;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "Timer", description = "Changes flow of time", category = Module.Category.WORLD)
public class Timer extends Module {
    public final SettingNumber multiplier = new SettingNumber.Builder("Multiplier").min(0).max(5).inc(0.1).value(1).setup(this);

    @Subscribe
    private void onBeginRenderTick(EventBeginRenderTick event) {
        event.setMultiplier((float) multiplier.getValue());
    }
}
