package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.util.Formatting;

@Command.Register(name = "toggle", description = "Toggles a module by name.", modifier = "module", aliases = "t")
public class Toggle extends Command {
	@Override
	public void onCommand(String[] args, String command) {
		if (args.length > 0) {
			if (Bloomware.moduleManager.getModule(args[0]) != null) {
				Bloomware.moduleManager.getModule(args[0]).toggle();
				CommandManager.addChatMessage(args[0] + " " + (Bloomware.moduleManager.getModule(args[0]).isEnabled() ? Formatting.GREEN + "enabled" + Formatting.GRAY + "." : Formatting.RED + "disabled" + Formatting.GRAY + "."));
			} else {
				CommandManager.addChatMessage(Formatting.DARK_RED + "module not found.");
			}
		} else {
			CommandManager.addChatMessage(CommandManager.USAGE + Formatting.RESET + Formatting.GOLD + CommandManager.prefix + "toggle <module>");
		}
	}
}