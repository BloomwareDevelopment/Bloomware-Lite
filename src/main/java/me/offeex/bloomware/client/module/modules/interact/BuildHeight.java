package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.mixins.accessors.IBlockHitResult;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.Direction;

@Module.Register(name = "BuildHeight", description = "Allows you to interact with utilities at utilities height", category = Module.Category.INTERACT)
public class BuildHeight extends Module {
    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            if (packet.getBlockHitResult().getPos().y >= 320 && packet.getBlockHitResult().getSide() == Direction.UP) {
                ((IBlockHitResult) packet.getBlockHitResult()).setSide(Direction.DOWN);
            }
        }
    }
}
