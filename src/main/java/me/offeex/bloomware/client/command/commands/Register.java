package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.serveraccount.ServerAccount;
import me.offeex.bloomware.api.serveraccount.ServerAccountManager;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

import java.util.Arrays;

@Command.Register(name = "register", description = "registers your alt on the server", modifier = "account", aliases = "reg")
public class Register extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0 && mc.getCurrentServerEntry().address != null) {
            switch (args[0]) {
                case "gen" -> {
                    if (args.length == 2) {
                        CommandManager.addChatMessage("Generating random password...");
                        String password = ServerAccountManager.generateRandomPassword();
                        CommandManager.addChatMessage("Generated a new password: " + password + "! Logging in...");
                        StringBuilder cmd = new StringBuilder("/register ");
                        Bloomware.serverAccountManager.addAccount(new ServerAccount(mc.player.getEntityName(), password, mc.getCurrentServerEntry().address));
                        for (int i = 0; i < Integer.parseInt(args[1]); i++) cmd.append(password).append(" ");
                        mc.player.sendChatMessage(cmd.toString());
                    } else {
                        CommandManager.addChatMessage(CommandManager.USAGE + "register gen <how many times do you need to repeat the password for registration>");
                    }
                }
                case "add" -> {
                    if (args.length >= 2) {
                        mc.player.sendChatMessage("/register " + String.join(" ", Arrays.copyOfRange(args, 1, args.length - 1)));
                        Bloomware.serverAccountManager.addAccount(new ServerAccount(mc.player.getEntityName(), args[1], mc.getCurrentServerEntry().address));
                    }
                    else CommandManager.addChatMessage(CommandManager.USAGE + "add <your password>");
                }
                default -> CommandManager.addChatMessage(CommandManager.USAGE + "add/gen <args>");
            }
        } else {
            CommandManager.addChatMessage(CommandManager.USAGE + "add/gen <args>");
        }
    }
}
