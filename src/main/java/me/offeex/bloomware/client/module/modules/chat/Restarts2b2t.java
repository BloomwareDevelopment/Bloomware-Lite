package me.offeex.bloomware.client.module.modules.chat;

import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Module.Register(name = "2b2tRestarts", description = "Notifies you when 2b2t restarts", category = Module.Category.CHAT)
public class Restarts2b2t extends Module {
    private final Thread t = new Thread(() -> {
        String old = "";
        InputStream connection;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                connection = new URL("http://crystalpvp.ru/restarts/fetch").openStream();
                String data = parseValue(new BufferedReader(new InputStreamReader(connection, StandardCharsets.UTF_8)).readLine());
                if (data != null) {
                    if (!data.equalsIgnoreCase("None") && !data.equals(old)) {
                        CommandManager.addChatMessage(data);
                        old = data;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    private String parseValue(String value) {
        String message = null;
        if (value != null) {
            if (value.contains("m")) message = "2b2t restarting in " + StringUtils.chop(value) + " minutes!";
            else if (value.contains("now")) message = "2b2t restarting!";
            else if (value.contains("s")) message = "2b2t restarting in 15 seconds!";
            else message = "None";
        }
        return message;
    }

    @Override
    public void onEnable() {
        t.start();
    }

    @Override
    public void onDisable() {
        t.interrupt();
    }
}
