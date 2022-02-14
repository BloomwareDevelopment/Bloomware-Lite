package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

@Module.Register(name = "SilentClose", description = "Makes the server think that you are still in container after closing it", category = Module.Category.INTERACT)
public class SilentClose extends Module {
    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        event.setCancelled(event.getPacket() instanceof CloseHandledScreenC2SPacket);
    }
}
