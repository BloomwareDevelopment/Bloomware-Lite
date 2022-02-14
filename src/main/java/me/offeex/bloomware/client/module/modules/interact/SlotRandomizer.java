package me.offeex.bloomware.client.module.modules.interact;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

import java.util.Random;

@Module.Register(name = "SlotRandomizer", description = "Randomizes selected slot", category = Module.Category.INTERACT)
public class SlotRandomizer extends Module {
    private final SettingNumber perTick = new SettingNumber.Builder("PerTick").value(1).min(1).max(10).inc(1).setup(this);
    private final Random random = new Random();

    @Override
    public void onTick() {
        for (int i = 0; i < perTick.getValue(); i++) {
            mc.player.getInventory().selectedSlot = random.nextInt(0, 8);
        }
    }
}
