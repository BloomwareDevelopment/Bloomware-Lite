package me.offeex.bloomware.client.module.modules.screen;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "CustomFOV", description = "Changes your FOV.", category = Module.Category.CAMERA)
public class CustomFOV extends Module {
    private final SettingNumber fov = new SettingNumber.Builder("FOV").value(110).min(30).max(170).inc(1).setup(this);
    private double oldFov = 90;

    @Override
    public void onEnable() {
        oldFov = mc.options.fov;
    }

    @Override
    public void onDisable() {
        mc.options.fov = oldFov;
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        mc.options.fov = fov.getValue();
    }
}
