package me.offeex.bloomware.client.module.modules.extension;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;

import java.awt.*;

@Module.Register(name = "Notifications", description = "Shows system notifications about your game", category = Module.Category.EXTENSION)
public class Notifications extends Module {
    private final SystemTray tray = SystemTray.getSystemTray();
    private final Image image = Toolkit.getDefaultToolkit().createImage(Bloomware.class.getResource("/assets/bloomware/elements/tray/icon32x32.png"));
    private TrayIcon icon;

    @Override
    public void onEnable() {
        if (!SystemTray.isSupported()) {
            CommandManager.addChatMessage("System notifications are not supported on your computer, disabling.");
            disable();
        } else {
            icon = new TrayIcon(image, "Bloomware Client");
            icon.setImageAutoSize(true);
            try {
                tray.add(icon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        tray.remove(icon);
    }

    public void showNotification(String text) {
        if (isEnabled()) icon.displayMessage("Bloomware", text, TrayIcon.MessageType.INFO);
    }
}
