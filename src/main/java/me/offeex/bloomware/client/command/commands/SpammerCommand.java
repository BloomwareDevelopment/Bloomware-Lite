package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

import java.util.Arrays;

@Command.Register(name = "spammer", description = "Modifies list of spammer messages", modifier = "no", aliases = "spam")
public class SpammerCommand extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("add")) {
                Bloomware.spammerManager.addMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                CommandManager.addChatMessage("Message was added to your spammer messages list!");
            } else {
                CommandManager.addChatMessage(CommandManager.USAGE + " add <message>");
            }
        } else {
            CommandManager.addChatMessage(CommandManager.USAGE + " add <message>");
        }
    }
}
