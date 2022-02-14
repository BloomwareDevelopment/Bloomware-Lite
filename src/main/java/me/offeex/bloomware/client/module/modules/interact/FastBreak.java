package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventBlockBreakingCooldown;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

@Module.Register(name = "FastBreak", description = "", category = Module.Category.INTERACT)
public class FastBreak extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Haste", "Off").setup(this);
    private final SettingNumber delay = new SettingNumber.Builder("Delay").value(1).min(0).max(1).inc(0.2).setup(this);
    private final SettingNumber hasteLevel = new SettingNumber.Builder("HasteLevel").value(2).min(1).max(3).inc(1).setup(this);
    private final SettingBool creative = new SettingBool.Builder("Creative").value(true).setup(this);

    private StatusEffectInstance haste;

    @Override
    public void onEnable() {
        haste = new StatusEffectInstance(StatusEffects.HASTE, Integer.MAX_VALUE, (int) hasteLevel.getValue() - 1);
    }

    @Override
    public void onDisable() {
        if (mode.is("Haste")) {
            mc.player.removeStatusEffect(StatusEffects.HASTE);
        }
    }

    @Override
    public void onTick() {
        if (mode.is("Haste")) mc.player.addStatusEffect(haste);
        else mc.player.removeStatusEffect(StatusEffects.HASTE);
    }

    @Subscribe
    public void onBlockBreakingCooldown(EventBlockBreakingCooldown event) {
        if (mc.player.isCreative() && !creative.getValue()) return;
        if (delay.getValue() != 1) event.setCooldown((int) (delay.getValue() * 5));
    }
}
