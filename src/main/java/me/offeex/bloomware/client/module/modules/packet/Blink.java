package me.offeex.bloomware.client.module.modules.packet;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventJoinWorld;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.ArrayList;
import java.util.UUID;

@Module.Register(name = "Blink", description = "Allows you to server-side teleport", category = Module.Category.PACKET)
public class Blink extends Module {
    private final SettingBool spawnDummy = new SettingBool.Builder("SpawnDummy").value(true).setup(this);

    private final ArrayList<PlayerMoveC2SPacket> packets = new ArrayList<>();
    private OtherClientPlayerEntity player;

    @Override
    public void onEnable() {
        if (spawnDummy.getValue()) {
            player = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
            player.copyFrom(mc.player);
            player.setUuid(UUID.randomUUID());
            mc.world.addEntity(-300, player);
        }
    }

    @Override
    public void onDisable() {
        packets.forEach(packet -> mc.getNetworkHandler().sendPacket(packet));
        packets.clear();
        mc.world.removeEntity(player.getId(), Entity.RemovalReason.DISCARDED);
    }

    @Subscribe
    private void onJoinWorld(EventJoinWorld event) {
        packets.clear();
        disable();
    }

    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            event.setCancelled(true);
            packets.add(packet);
        }
    }
}
