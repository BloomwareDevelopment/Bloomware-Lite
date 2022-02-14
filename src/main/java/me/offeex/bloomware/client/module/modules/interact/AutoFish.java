package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.util.Hand;

@Module.Register(name = "AutoFish", description = "Automatically fishing", category = Module.Category.INTERACT)
public class AutoFish extends Module {
    private final SettingNumber delay = new SettingNumber.Builder("Delay").value(20).min(1).max(40).setup(this);
    private boolean toThrow = false;

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound().getId().toString().equalsIgnoreCase("minecraft:entity.fishing_bobber.splash")) {
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                toThrow = true;
            }
        }
    }

    @Override
    public void onTick() {
        if (toThrow) {
            if (mc.player.age == delay.getValue()) {
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                toThrow = false;
            }
        }
    }
}
