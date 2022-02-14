package me.offeex.bloomware.client.module.modules.motion;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "Step", description = "", category = Module.Category.MOTION)
public class Step extends Module {
    private final SettingNumber height = new SettingNumber.Builder("Height").min(1).max(3).value(2).inc(0.1).setup(this);

    @Override public void onTick() {
        mc.player.stepHeight = (float) height.getValue();
    }

    @Override public void onDisable() {
        if (mc.player != null) mc.player.stepHeight = 0.6f;
    }

}
