package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPlayerMove;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;

@Module.Register(name = "Sprint", description = "Sprinting you", category = Module.Category.MOTION)
public class Sprint extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Legit", "Rage").selected("Rage").setup(this);

    public void onDisable() {
        mc.player.setSprinting(false);
        mc.options.keySprint.setPressed(false);
    }

    @Subscribe
    private void onPlayerMove(EventPlayerMove e) {
        if(mc.player.getHungerManager().getFoodLevel() <= 6) return;
        switch (mode.getSelectedStr()) {
            case "Legit" -> mc.options.keySprint.setPressed(true);
            case "Rage" -> mc.player.setSprinting(true);
        }
    }
}