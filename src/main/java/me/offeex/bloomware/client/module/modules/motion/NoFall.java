package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import me.offeex.bloomware.mixins.accessors.IPlayerMoveC2SPacket;
import net.minecraft.block.AirBlock;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@Module.Register(name = "NoFall", description = "Allows you not to take damage when you fall.", category = Module.Category.MOTION)
public class NoFall extends Module {
    private final SettingNumber fallDistance = new SettingNumber.Builder("FallDistance").value(2.5).min(2.5).max(100).inc(0.5).setup(this);
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Packet", "Anti").selected("Packet").setup(this);

    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && mode.is("Packet")) {
            if (mc.player.fallDistance > fallDistance.getValue() && mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock() instanceof AirBlock)
                ((IPlayerMoveC2SPacket) packet).setOnGround(true);
        }
    }
}
