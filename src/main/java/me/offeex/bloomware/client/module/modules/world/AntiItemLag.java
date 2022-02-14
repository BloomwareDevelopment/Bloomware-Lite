package me.offeex.bloomware.client.module.modules.world;

import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "AntiItemLag", description = "Allows you to bypass lags with too long item name", category = Module.Category.WORLD)
public class AntiItemLag extends Module {
    private final SettingNumber maxLength = new SettingNumber.Builder("MaxLength").value(28).min(1).max(100).inc(1).setup(this);
    private final SettingBool notify = new SettingBool.Builder("Notify").value(true).setup(this);

    @Override
    public void onTick() {
        if (mc.player.getMainHandStack().hasCustomName()) {
            if (mc.player.getMainHandStack().getName().asString().length() >= maxLength.getValue()) {
                mc.player.getMainHandStack().removeCustomName();
                if (notify.getValue()) CommandManager.addChatMessage("Found lag item! Fixing...");
            }
        }
    }
}
