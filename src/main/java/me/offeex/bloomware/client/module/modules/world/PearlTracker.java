package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;

@Module.Register(name = "PearlTracker", description = "Tracks interact when it teleports by pearl", category = Module.Category.WORLD)
public class PearlTracker extends Module {
    private final ArrayList<PlayerEntity> targetPlayers = new ArrayList<>();

    /* DOESN'T WORK!!! */
    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof ChunkLoadDistanceS2CPacket packet) {
            System.out.println(packet.getDistance());
        }
        if (event.getPacket() instanceof ChunkRenderDistanceCenterS2CPacket packet) {
            System.out.println(packet.getChunkX() + " " + packet.getChunkZ());
        }
        if (event.getPacket() instanceof ChunkDataS2CPacket packet) {
            System.out.println(packet.getX() + " " + packet.getZ());
        }
    }

    @Override
    public void onTick() {
        mc.world.getPlayers().stream().filter(player -> player.getName() != mc.player.getName()).forEach(player -> {
            if (targetPlayers.contains(player) && player.distanceTo(mc.player) <= mc.options.viewDistance) {
                CommandManager.addChatMessage("Removing " + player.getName() + " from list, they are now in range at " + player.distanceTo(mc.player) + " distance");
                targetPlayers.remove(player);
            }

            if (player.distanceTo(mc.player) > mc.options.viewDistance * 16) {
                targetPlayers.add(player);
                CommandManager.addChatMessage(player.getName().asString() + " just teleported to " + (int) player.getX() + ", " + (int) player.getZ());
            }
        });
    }
}
