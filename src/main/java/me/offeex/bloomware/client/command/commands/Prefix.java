package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.util.Formatting;

@Command.Register(name = "prefix", description = "change the prefix", modifier = "key", aliases = "p")
public class Prefix extends Command {
	@Override
	public void onCommand(String[] args, String command) {
		if (args.length == 1 && args[0].length() == 1) {
			CommandManager.setCommandPrefix(args[0]);
			CommandManager.addChatMessage(Formatting.GREEN + "prefix " + Formatting.GRAY + "was set to " + Formatting.GREEN + CommandManager.prefix);
		}
		else CommandManager.addChatMessage(CommandManager.USAGE + Formatting.RESET + Formatting.GOLD + CommandManager.prefix + "prefix <key>");
	}
}
