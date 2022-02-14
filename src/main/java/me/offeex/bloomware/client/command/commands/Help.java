package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

import java.util.stream.Collectors;

@Command.Register(name = "help", description = "Shows list of commands.", modifier = "", aliases = "h")
public class Help extends Command {
	@Override
	public void onCommand(String[] args, String command) {
		CommandManager.addChatMessage("Commands (" + CommandManager.commands.size() + "): " + CommandManager.commands.stream().map(Command::getName).collect(Collectors.joining(", ")));
	}
}