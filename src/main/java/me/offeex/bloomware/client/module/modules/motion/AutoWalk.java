package me.offeex.bloomware.client.module.modules.motion;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;

@Module.Register(name = "AutoWalk", description = "Allows you to go automatically", category = Module.Category.MOTION)
public class AutoWalk extends Module {
    public final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Normal", "Baritone").setup(this);

    @Override
    public void onTick() {
        mc.options.keyForward.setPressed(true);
    }

    @Override
    public void onDisable() {
        mc.options.keyForward.setPressed(false);
    }
}
