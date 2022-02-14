package me.offeex.bloomware.client.command.commands;

import baritone.api.BaritoneAPI;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

@Command.Register(name = "baritone", description = "Allows you to execute baritone commands", modifier = "no", aliases = "bar")
public class BaritoneCommand extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(String.join(" ", args));
        } else {
            CommandManager.addChatMessage("Cannot execute baritone command!");
        }
    }
}
