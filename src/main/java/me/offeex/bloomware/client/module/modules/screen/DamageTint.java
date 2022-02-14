package me.offeex.bloomware.client.module.modules.screen;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "DamageTint", description = "Shows red overlay when your health is low", category = Module.Category.CAMERA)
public class DamageTint extends Module {
    public final SettingNumber health = new SettingNumber.Builder("Threshold").min(1).max(36).value(12).inc(1).setup(this);
    public final SettingNumber power = new SettingNumber.Builder("Amplifier").min(0).max(1).value(1).inc(0.1).setup(this);

    /**
     * @see me.offeex.bloomware.mixins.MixinIngameHud
     */
}
