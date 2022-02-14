package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.Command;
import me.offeex.bloomware.client.command.CommandManager;

@Command.Register(name = "session", description = "shows info about your current session", modifier = "no", aliases = "ses")
public class Session extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        CommandManager.addChatMessage(String.format("Your current session stats: %s kills, %s deaths, %s pops. Online for %s", Bloomware.sessionManager.getKills(), Bloomware.sessionManager.getDeaths(),
                Bloomware.sessionManager.getPops(), Bloomware.sessionManager.convertTime(Bloomware.sessionManager.getTimeOnline())));
    }
}
