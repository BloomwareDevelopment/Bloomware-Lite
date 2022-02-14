package me.offeex.bloomware.api.manager.managers;

import com.google.gson.*;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.gui.screen.frame.Frame;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.*;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ConfigManager {
    private final File directory = Bloomware.fileManager.getConfigDirectory();
    private final Gson gson = new Gson();

    public void loadData() throws IOException {
        Bloomware.moduleManager.getModules().forEach(this::loadDataFromJson);
        loadPrefix();
        loadGui();
    }

    private void loadDataFromJson(Module module) {
        Path path = Path.of(directory + "/" + module.getName() + ".json");
        try {
            String content = Bloomware.fileManager.loadFileContent(path);
            if (content == null) return;
                JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
                try {
                    if (jsonObject.get("enabled").getAsBoolean()) module.enable();
                } catch (Exception ignored) {}
                module.setKey(jsonObject.get("bind").getAsInt());
                module.setX(jsonObject.get("x_coord").getAsInt());
                module.setY(jsonObject.get("y_coord").getAsInt());

                Bloomware.settingManager.getSettings(module).forEach(setting -> {
                    JsonElement element = jsonObject.get(setting.getId());
                    if (element != null) {
                        try {
                            switch (setting.getType()) {
                                case "SettingBool" -> ((SettingBool) setting).setValue(element.getAsBoolean());
                                case "SettingEnum" -> ((SettingEnum) setting).setSelected(element.getAsString());
                                case "SettingNumber" -> ((SettingNumber) setting).setValue(element.getAsDouble());
                                case "SettingColor" -> ((SettingColor) setting).getColor().setColor(element.getAsInt());
                                case "SettingGroup" -> {
                                    if (((SettingGroup) setting).isToggleable())
                                        ((SettingGroup) setting).setToggled(element.getAsBoolean());
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            Bloomware.logger.error("Found bad config for " + module.getName() + ". Restoring default settings.");
                        }
                    }
                });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        Bloomware.moduleManager.getModules().forEach(module -> {
            File moduleFile = new File(directory + "/" + module.getName() + ".json");
            if (!moduleFile.exists()) Bloomware.fileManager.createFile(moduleFile);
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(moduleFile.toPath())));
                writer.write(gson.toJson(convertToJson(module)));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            savePrefix();
            saveGui();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private JsonObject convertToJson(Module module) {
        JsonObject object = new JsonObject();
        object.add("enabled", new JsonPrimitive(module.isEnabled()));
        object.add("bind", new JsonPrimitive(module.getKey()));

        object.add("x_coord", new JsonPrimitive(module.x));
        object.add("y_coord", new JsonPrimitive(module.y));

        Bloomware.settingManager.getSettings(module).forEach(setting -> {
            switch (setting.getType()) {
                case "SettingBool" -> object.add(setting.getId(), new JsonPrimitive(((SettingBool) setting).getValue()));
                case "SettingNumber" -> object.add(setting.getId(), new JsonPrimitive(((SettingNumber) setting).getValue()));
                case "SettingEnum" -> object.add(setting.getId(), new JsonPrimitive(((SettingEnum) setting).getSelectedStr()));
                case "SettingColor" -> object.add(setting.getId(),  new JsonPrimitive(((SettingColor) setting).getColor().getRGB()));
                case "SettingGroup" -> {
                    if (((SettingGroup) setting).isToggleable()) object.add(setting.getId(), new JsonPrimitive(((SettingGroup) setting).isToggled()));
                }
            }
        });
        return object;
    }

    private void savePrefix() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(Bloomware.fileManager.getPrefixDirectory());
        writer.print("");
        writer.write(CommandManager.prefix);
        writer.close();
    }

    private void loadPrefix() throws FileNotFoundException {
        String prefix = Bloomware.fileManager.loadFileContent(Bloomware.fileManager.getPrefixDirectory().toPath());
        if (!prefix.equals("")) CommandManager.prefix = String.valueOf(prefix.charAt(0));
    }

    private void saveGui() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(Bloomware.fileManager.getGuiDirectory());
        writer.print("");
        Bloomware.gui.getFrames().forEach(frame -> writer.write(frame.category + ":" + frame.getX() + ":" + frame.getY() + "\n"));
        writer.close();
    }

    private void loadGui() throws FileNotFoundException {
        String[] content = Bloomware.fileManager.loadFileContent(Bloomware.fileManager.getGuiDirectory().toPath()).split("\n");
        for (String str : content) {
            String[] data = str.strip().split(":");
            Frame frame = Bloomware.gui.getFrameByCategory(str.split(":")[0]);
            if (frame != null) {
                frame.setX(Integer.parseInt(data[1]));
                frame.setY(Integer.parseInt(data[2]));
            }
        }
    }
}
