package me.offeex.bloomware.client.command.commands;

import me.offeex.bloomware.client.command.Command;

@Command.Register(name = "clear", description = "Clears your chat", modifier = "chat", aliases = "clearchat")
public class ClearChat extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        mc.inGameHud.getChatHud().clear(true);
    }
}
