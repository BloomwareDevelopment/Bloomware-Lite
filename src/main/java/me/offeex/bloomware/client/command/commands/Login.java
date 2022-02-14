package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.serveraccount.ServerAccount;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

@Command.Register(name = "login", description = "automatically logins to servers with your saved credentials", modifier = "account", aliases = "log")
public class Login extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (mc.getCurrentServerEntry().address != null) {
            ServerAccount account = Bloomware.serverAccountManager.findAccount(mc.player.getEntityName(), mc.getCurrentServerEntry().address);
            if (account != null) {
                CommandManager.addChatMessage("Found credentials to account! Logging in...");
                mc.player.sendChatMessage("/login " + account.password());
            } else {
                CommandManager.addChatMessage("Credentials not found!");
            }
        } else {
            CommandManager.addChatMessage("You are in singleplayer!");
        }
    }
}
