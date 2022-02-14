package me.offeex.bloomware.client.command.commands;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.suggestion.Suggestion;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

@Command.Register(name = "plugins", description = "grabs plugins from server.", modifier = "", aliases = "plugs")
public class Plugins extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        CommandManager.addChatMessage("Grabbing server plugins...");
        mc.getNetworkHandler().sendPacket(new RequestCommandCompletionsC2SPacket(0, "/"));
        Bloomware.EVENTBUS.register(this);
    }

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof CommandSuggestionsS2CPacket) {
            ArrayList<String> commands = new ArrayList<>();
            for (Suggestion suggestion : ((CommandSuggestionsS2CPacket) event.getPacket()).getSuggestions().getList()) {
                String plugin = suggestion.getText().split(":")[0];
                if (suggestion.getText().contains(":") && !plugin.equalsIgnoreCase("minecraft") && !plugin.equalsIgnoreCase("bukkit") && !commands.contains(plugin)) commands.add(plugin);
            }
            CommandManager.addChatMessage("Plugins (" + commands.size() + "): " + StringUtils.join(commands, ", "));
            Bloomware.EVENTBUS.unregister(this);
        }
    }
}
