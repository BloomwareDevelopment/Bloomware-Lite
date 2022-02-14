package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventBreakBlock;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.Direction;

@Module.Register(name = "NoGhostBlock", description = "Prevents the game from creating ghost blocks", category = Module.Category.INTERACT)
public class NoGhostBlock extends Module {
    @Subscribe private void onBreakBlock(EventBreakBlock event) {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, event.getPos(), Direction.UP));
    }
}
