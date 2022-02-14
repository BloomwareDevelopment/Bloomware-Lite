package me.offeex.bloomware.client.module.modules.packet;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;

@Module.Register(name = "PacketCancel", description = "Cancels packets.", category = Module.Category.PACKET)
public class PacketCancel extends Module {
    private final SettingBool c2s = new SettingBool.Builder("C2SPackets").value(true).setup(this);
    private final SettingBool s2c = new SettingBool.Builder("S2CPackets").value(false).setup(this);

    @Subscribe
    private void onPacket(EventPacket event) {
        event.setCancelled(event instanceof EventPacket.Receive && s2c.getValue());
        event.setCancelled(event instanceof EventPacket.Send && c2s.getValue());
    }
}
