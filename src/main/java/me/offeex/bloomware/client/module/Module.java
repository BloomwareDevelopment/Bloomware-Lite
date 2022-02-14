package me.offeex.bloomware.client.module;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.hud.ModuleNotifier;
import me.offeex.bloomware.client.setting.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Module {
    protected static final MinecraftClient mc = Bloomware.mc;
    private final String name, description;
    private int key;
    private final Category category;
    private boolean enabled;
    public int x = 10, y = 100, width, height = 16;

    public Module() {
        Register info = getClass().getAnnotation(Register.class);
        this.name = info.name();
        this.description = info.description();
        this.category = info.category();
    }

    public enum Category {
        PVP("PvP", Color.decode("#ff2e2e")),
        MOTION("Travel", Color.decode("#edd628")),
        INTERACT("Interact", Color.decode("#23e823")),
        CAMERA("Camera", Color.decode("#23e8ad")),
        PACKET("Packet", Color.decode("#3b41eb")),
        VISUALS("Visuals", Color.decode("#23e1e8")),
        WORLD("World", Color.decode("#d6eb3b")),
        CHAT("Chat", Color.decode("#526cff")),
        EXTENSION("Extension", Color.decode("#eb763b")),
        CLIENT("Client", Color.white),
        HUD("HUD", Color.white);

        private final String name;
        private final Color color;

        Category(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }
    }

    public String getName() {
        return this.name;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getDescription() {
        return description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void toggle() {
        if (enabled) disable();
        else enable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        try {
            ((ModuleNotifier) Bloomware.moduleManager.getModule(ModuleNotifier.class)).setMessage(this.name + " enabled!");
            Bloomware.EVENTBUS.register(this);
            setEnabled(true);
            onEnable();
        } catch (Exception ignored) {}
    }

    public void disable() {
        try {
            ((ModuleNotifier) Bloomware.moduleManager.getModule(ModuleNotifier.class)).setMessage(this.name + " disabled!");
            Bloomware.EVENTBUS.unregister(this);
            setEnabled(false);
            onDisable();
        } catch (Exception ignored) {}
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTick() {
    }

    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
    }

    public Setting getSetting(String name) {
        return Bloomware.settingManager.getSettings(this).stream().filter(setting -> setting.getName().equals(name)).findAny().orElse(null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Register {
        String name();

        String description();

        Category category();
    }
}
