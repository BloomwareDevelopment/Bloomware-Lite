package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

@Command.Register(name = "server", description = "Shows what server you are playing", modifier = "no", aliases = "serv")
public class Server extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        CommandManager.addChatMessage("You are playing on " + getServer());
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("copy")) {
                mc.keyboard.setClipboard(getServer());
                CommandManager.addChatMessage("Server IP was copied to your clipboard!");
            }
        }
    }

    private String getServer() {
        if (mc.getCurrentServerEntry() != null) return mc.getCurrentServerEntry().address;
        else if (mc.isInSingleplayer()) return "Singleplayer";
        else return "null";
    }
}
