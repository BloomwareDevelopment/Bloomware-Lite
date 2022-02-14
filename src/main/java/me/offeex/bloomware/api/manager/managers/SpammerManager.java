package me.offeex.bloomware.api.manager.managers;

import me.offeex.bloomware.Bloomware;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class SpammerManager {
    private final ArrayList<String> messages = new ArrayList<>();

    public SpammerManager() {
        loadMessages();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void loadMessages() {
        try {
            messages.addAll(Arrays.asList(Bloomware.fileManager.loadFileContent(Bloomware.fileManager.getSpammerDirectory().toPath()).split("\n")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveMessages() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(Bloomware.fileManager.getSpammerDirectory());
        writer.print("");
        messages.forEach(message -> writer.write(message + "\n"));
        writer.close();
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}
