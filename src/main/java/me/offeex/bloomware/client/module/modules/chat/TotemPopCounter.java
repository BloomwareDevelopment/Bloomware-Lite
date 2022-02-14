package me.offeex.bloomware.client.module.modules.chat;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;

@Module.Register(name = "TotemPopCounter", description = "Counts totem pops.", category = Module.Category.CHAT)
public class TotemPopCounter extends Module {
    private final HashMap<String, Integer> pops = new HashMap<>();

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (packet.getStatus() == 35) applyPop(packet.getEntity(mc.world));
        }
    }

    @Override
    public void onTick() {
        mc.world.getPlayers().stream().filter(p -> p.getHealth() <= 0 && pops.containsKey(p.getEntityName())).forEach(player -> {
            CommandManager.addChatMessage(player.getEntityName() + " died after popping " + pops.get(player.getEntityName()) + " totem(s)!");
            pops.remove(player.getEntityName(), pops.get(player.getEntityName()));
        });
    }

    @Override
    public void onDisable() {
        pops.clear();
    }

    private void applyPop(Entity entity) {
        pops.put(entity.getEntityName(), pops.get(entity.getEntityName()) == null ? 1 : pops.get(entity.getEntityName()) + 1);
        CommandManager.addChatMessage(entity.getEntityName() + " popped " + pops.get(entity.getEntityName()) + " totem(s)!");
    }
}
