package me.offeex.bloomware.client.module.modules.interact;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "Reach", description = "Lengthens your hands.", category = Module.Category.INTERACT)
public class Reach extends Module {
    public final SettingNumber amount = new SettingNumber.Builder("Amount").value(5).min(1).max(6).inc(0.1).setup(this);
}
