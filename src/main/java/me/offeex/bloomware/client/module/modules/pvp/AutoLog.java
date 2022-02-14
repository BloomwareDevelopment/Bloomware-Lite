package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

@Module.Register(name = "AutoLog", description = "Disconnects you when your health is low.", category = Module.Category.PVP)
public class AutoLog extends Module {
    private final SettingBool disableAfterLog = new SettingBool.Builder("DisableAfterLogout").value(true).setup(this);
    private final SettingNumber health = new SettingNumber.Builder("Health").value(2.0).min(1.0).max(20.0).inc(1.0).setup(this);
    private final SettingBool checkTotems = new SettingBool.Builder("LogoutAfterNoTotem").value(true).setup(this);

    @Override
    public void onTick() {
        if (mc.player.getHealth() <= health.getValue()) {
            if (checkTotems.getValue()) {
                if (mc.player.getInventory().count(Items.TOTEM_OF_UNDYING) == 0) {
                    mc.getNetworkHandler().getConnection().disconnect(Text.of("Kicked due AutoLog."));
                }
            } else {
                mc.getNetworkHandler().getConnection().disconnect(Text.of("Kicked due AutoLog."));
            }
            if (disableAfterLog.getValue()) {
                this.disable();
            }
        }
    }
}
