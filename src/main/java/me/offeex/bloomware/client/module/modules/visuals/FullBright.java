package me.offeex.bloomware.client.module.modules.visuals;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

@Module.Register(name = "FullBright", description = "Boosts your brightness", category = Module.Category.VISUALS)
public class FullBright extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Gamma", "Potion").selected("Gamma").setup(this);
    private final SettingNumber gamma = new SettingNumber.Builder("Gamma").value(4).min(1).max(4).inc(1).setup(this);

    private double oldBrightness = 1;

    @Override
    public void onEnable() {
        if (mode.is("Gamma")) {
            oldBrightness = mc.options.gamma;
            mc.options.gamma = gamma.getValue();
        }
    }

    @Override
    public void onDisable() {
        mc.options.gamma = oldBrightness;
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }

    @Override
    public void onTick() {
        if (mode.is("Gamma")) {
            mc.options.gamma = gamma.getValue();
        } else if (mode.is("Potion")) mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 500, 0));
    }
}
