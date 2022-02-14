package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.util.Formatting;

@Command.Register(name = "friend", description = "allows you to add and remove friends", modifier = "", aliases = "")
public class FriendCommand extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            switch (args[0]) {
                case "remove" -> {
                    Bloomware.friendManager.removePerson(args[1]);
                    CommandManager.addChatMessage(Formatting.DARK_RED + args[1] + " removed from your friend list!");
                }
                case "add" -> {
                    Bloomware.friendManager.addPerson(args[1], FriendManager.PersonType.FRIEND);
                    CommandManager.addChatMessage(Formatting.GREEN + args[1] + " added to your friend list!");
                }
                default -> CommandManager.addChatMessage(CommandManager.USAGE + " add/remove <nickname>");
            }
        } else {
            CommandManager.addChatMessage(CommandManager.USAGE + " add/remove <nickname>");
        }
    }
}
