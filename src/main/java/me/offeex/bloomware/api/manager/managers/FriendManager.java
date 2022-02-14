package me.offeex.bloomware.api.manager.managers;

import me.offeex.bloomware.Bloomware;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;


public class FriendManager {
    private final File data = new File(new File(MinecraftClient.getInstance().runDirectory, Bloomware.NAME), "people.txt");
    private final HashMap<String, PersonType> people = new HashMap<>();

    public void loadData() {
        try {
            if (!data.exists()) data.createNewFile();
            Files.readAllLines(data.toPath()).forEach(line -> people.put(line.split(":")[0], PersonType.valueOf(line.split(":")[1])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(data);
        writer.print("");
        people.forEach((nickname, type) -> writer.write(nickname + ":" + type + "\n"));
        writer.close();
    }

    public PersonType getType(String nickname) {
        return people.get(nickname);
    }

    public void addPerson(String nickname, PersonType type) {
        people.remove(nickname);
        people.put(nickname, type);
    }

    public void removePerson(String nickname) {
        people.remove(nickname);
    }

    public enum PersonType {
        FRIEND, ENEMY
    }
}
