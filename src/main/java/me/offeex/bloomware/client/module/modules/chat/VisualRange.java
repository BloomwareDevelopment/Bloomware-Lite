package me.offeex.bloomware.client.module.modules.chat;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventAddedEntity;
import me.offeex.bloomware.api.event.events.EventRemovedEntity;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.entity.player.PlayerEntity;


@Module.Register(name = "VisualRange", description = "Notifies you when entity appears/disappears from your visual range", category = Module.Category.CHAT)
public class VisualRange extends Module {
    @Subscribe
    private void onEntityAdded(EventAddedEntity event) {
        if (event.getEntity() instanceof PlayerEntity player && event.getEntity() != mc.player) CommandManager.addChatMessage(player.getEntityName() + " has entered your visual range!");
    }

    @Subscribe
    private void onEntityRemoved(EventRemovedEntity event) {
        if (event.getEntity() instanceof PlayerEntity player && event.getEntity() != mc.player) CommandManager.addChatMessage(player.getEntityName() + " has left your visual range!");
    }
}
