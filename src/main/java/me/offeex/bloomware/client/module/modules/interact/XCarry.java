package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

@Module.Register(name = "XCarry", description = "Allows you to keep items in crafting slots", category = Module.Category.INTERACT)
public class XCarry extends Module {
    private CloseHandledScreenC2SPacket lastPacket = null;

    @Override
    public void onDisable() {
        if (lastPacket != null) mc.player.networkHandler.sendPacket(lastPacket);
        lastPacket = null;
    }

    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof CloseHandledScreenC2SPacket packet) {
            if (packet.getSyncId() == mc.player.playerScreenHandler.syncId) {
                event.setCancelled(true);
                lastPacket = packet;
            }
        }
    }
}
