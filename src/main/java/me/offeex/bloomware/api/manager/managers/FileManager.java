package me.offeex.bloomware.api.manager.managers;

import me.offeex.bloomware.Bloomware;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.Path;

public class FileManager {
    private final File mainDirectory = new File(MinecraftClient.getInstance().runDirectory, Bloomware.NAME);
    private final File configDirectory = new File(mainDirectory + "/config/");
    private final File peopleDirectory = new File(mainDirectory + "/people.txt");
    private final File spammerDirectory = new File(mainDirectory + "/spammer.txt");
    private final File prefixDirectory = new File(mainDirectory + "/prefix.txt");
    private final File proxyDirectory = new File(mainDirectory + "/proxies.json");
    private final File guiDirectory = new File(mainDirectory + "/gui.txt");

    public void initClientFiles() {
        try {
            if (!mainDirectory.exists()) mainDirectory.mkdir();
            if (!configDirectory.exists()) configDirectory.mkdir();
            if (!peopleDirectory.exists()) peopleDirectory.createNewFile();
            if (!spammerDirectory.exists()) spammerDirectory.createNewFile();
            if (!proxyDirectory.exists()) proxyDirectory.createNewFile();
            if (!prefixDirectory.exists()) prefixDirectory.createNewFile();
            if (!guiDirectory.exists()) guiDirectory.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getConfigDirectory() {
        return configDirectory;
    }

    public File getSpammerDirectory() {
        return spammerDirectory;
    }

    public File getProxyDirectory() {
        return proxyDirectory;
    }

    public File getMainDirectory() {
        return mainDirectory;
    }

    public File getPrefixDirectory() {
        return prefixDirectory;
    }

    public File getGuiDirectory() {
        return guiDirectory;
    }

    public void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadFileContent(Path path) throws FileNotFoundException {
        if (!path.toFile().exists()) return null;
        FileInputStream reader = new FileInputStream(path.toAbsolutePath().toFile());
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(reader));
        br.lines().forEach(line -> stringBuilder.append(line).append("\n"));
        return stringBuilder.toString();
    }
}
