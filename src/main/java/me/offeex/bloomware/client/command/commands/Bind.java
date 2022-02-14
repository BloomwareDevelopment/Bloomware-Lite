package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

@Command.Register(name = "bind", description = "set bind of module", modifier = "module", aliases = "b")
public class Bind extends Command {
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            if (Bloomware.moduleManager.getModule(args[0]) != null) {
                try {
                    if (args[1].equalsIgnoreCase("null") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("unbind")) {
                        Bloomware.moduleManager.getModule(args[0]).setKey(-1);
                        CommandManager.addChatMessage(Formatting.GREEN + Bloomware.moduleManager.getModule(args[0]).getName() + " was unbound");
                    } else {
                        Bloomware.moduleManager.getModule(args[0]).setKey(InputUtil.fromTranslationKey("key.keyboard." + args[1].toLowerCase()).getCode());
                        CommandManager.addChatMessage(Formatting.GREEN + Bloomware.moduleManager.getModule(args[0]).getName() + " was bound to " + args[1].toUpperCase() + "");
                    }
                } catch (Exception e) {
                    CommandManager.addChatMessage(Formatting.RED + "Invalid Syntax. Usage: " + CommandManager.prefix + "bind <Module> <Key>");
                }
            } else {
                CommandManager.addChatMessage(Formatting.RED + "Module not found.");
            }
        } else {
            CommandManager.addChatMessage(Formatting.RED + "Invalid Syntax. Usage: " + CommandManager.prefix + "bind <Module> <Key>");
        }
    }
}