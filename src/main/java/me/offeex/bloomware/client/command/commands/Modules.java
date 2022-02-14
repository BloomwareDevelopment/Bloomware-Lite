package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.util.Formatting;

@Command.Register(name = "modules", description = "shows info about modules", modifier = "", aliases = "mods")
public class Modules extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        Bloomware.moduleManager.getModules().forEach(module -> CommandManager.addChatMessage(Formatting.RED + module.getName() + CommandManager.ARROW + Formatting.RESET + Formatting.YELLOW + Formatting.ITALIC + module.getDescription()));
    }
}
