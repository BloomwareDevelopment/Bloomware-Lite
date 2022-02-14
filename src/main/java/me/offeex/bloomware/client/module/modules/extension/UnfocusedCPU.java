package me.offeex.bloomware.client.module.modules.extension;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "UnfocusedCPU", description = "Stops game rendering if you are not focused on window", category = Module.Category.EXTENSION)
public class UnfocusedCPU extends Module {
    public final SettingNumber limit = new SettingNumber.Builder("FPSLimit").value(1).min(1).max(60).inc(1).setup(this);
}
