package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.util.Formatting;

import java.util.stream.Collectors;

@Command.Register(name = "modulelist", description = "gets a list of the modules.", modifier = "", aliases = "ml")
public class ModuleList extends Command {
	@Override
	public void onCommand(String[] args, String command) {
		CommandManager.addChatMessage(args.length > 0 ? CommandManager.USAGE + Formatting.RESET + Formatting.GOLD + CommandManager.prefix + "modulelist" : "Modules: " + Formatting.GRAY + Bloomware.moduleManager.getModules().stream().map(Module::getName).collect(Collectors.joining(", ")));
	}
}
