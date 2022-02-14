package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;

@Module.Register(name = "AutoRespawn", description = "Allows you to respawn automatically", category = Module.Category.WORLD)
public class AutoRespawn extends Module {
    @Subscribe
    private void onOpenScreen(EventOpenScreen event) {
        if (event.getScreen() instanceof DeathScreen) mc.getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
    }
}
