package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

@Module.Register(name = "AntiParticleLag", description = "Prevents you from lags of big count of particles.", category = Module.Category.WORLD)
public class AntiParticleLag extends Module {
    private final SettingNumber particleLimit = new SettingNumber.Builder("ParticleLimit").value(100).min(1).max(1000).inc(1).setup(this);
    private final SettingBool notify = new SettingBool.Builder("Notify").value(true).setup(this);

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof ParticleS2CPacket packet) {
            if (packet.getCount() >= particleLimit.getValue()) {
                event.setCancelled(true);
                if (notify.getValue()) CommandManager.addChatMessage("Received packet with " + packet.getCount() + " particles! Canceling...");
            }
        }
    }
}
