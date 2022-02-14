package me.offeex.bloomware.client.module.modules.world;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.entity.effect.StatusEffects;

@Module.Register(name = "AntiEffect", description = "Removes negative effects", category = Module.Category.WORLD)
public class AntiEffect extends Module {
    private final SettingBool levitation = new SettingBool.Builder("Levitation").value(true).setup(this);
    private final SettingBool blindness = new SettingBool.Builder("Blindness").value(true).setup(this);
    private final SettingBool nausea = new SettingBool.Builder("Nausea").value(true).setup(this);
    private final SettingBool miningFatigue = new SettingBool.Builder("MiningFatigue").value(true).setup(this);
    private final SettingBool slowFalling = new SettingBool.Builder("SlowFalling").value(false).setup(this);

    @Override public void onTick() {
        if (levitation.getValue()) mc.player.removeStatusEffect(StatusEffects.LEVITATION);
        if (blindness.getValue()) mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        if (nausea.getValue()) mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        if (miningFatigue.getValue()) mc.player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        if (slowFalling.getValue()) mc.player.removeStatusEffect(StatusEffects.SLOW_FALLING);
    }
}
