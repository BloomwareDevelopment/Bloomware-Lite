package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.util.Formatting;

@Command.Register(name = "enemy", description = "Adds/deletes your enemies", modifier = "no", aliases = "")
public class Enemy extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            switch (args[0]) {
                case "add" -> {
                    Bloomware.friendManager.addPerson(args[1], FriendManager.PersonType.ENEMY);
                    CommandManager.addChatMessage(Formatting.GREEN + args[1] + " added to your enemy list!");
                }
                case "remove" -> {
                    Bloomware.friendManager.removePerson(args[1]);
                    CommandManager.addChatMessage(Formatting.DARK_RED + args[1] + " removed from your enemy list!");
                }
                default -> CommandManager.addChatMessage(CommandManager.USAGE + " add/remove <nickname>");
            }
        } else {
            CommandManager.addChatMessage(CommandManager.USAGE + " add/remove <nickname>");
        }
    }
}
