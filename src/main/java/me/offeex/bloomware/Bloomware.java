package me.offeex.bloomware;

import com.google.common.eventbus.EventBus;
import me.offeex.bloomware.api.manager.managers.ConfigManager;
import me.offeex.bloomware.api.proxy.ProxyManager;
import me.offeex.bloomware.api.serveraccount.ServerAccountManager;
import me.offeex.bloomware.api.manager.managers.SessionManager;
import me.offeex.bloomware.api.manager.managers.FileManager;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.api.manager.managers.SpammerManager;
import me.offeex.bloomware.api.util.APIChecker;
import me.offeex.bloomware.api.util.FontUtil;
import me.offeex.bloomware.api.util.SoundUtil;
import me.offeex.bloomware.api.manager.managers.RotationManager;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.gui.screen.accounteditor.AccountEditor;
import me.offeex.bloomware.client.gui.screen.ClickGUI;
import me.offeex.bloomware.client.gui.screen.HudEditor;
import me.offeex.bloomware.client.gui.screen.frame.component.FontRenderer;
import me.offeex.bloomware.client.gui.screen.overlay.Overlay;
import me.offeex.bloomware.client.gui.screen.overlay.musicplayer.MusicManager;
import me.offeex.bloomware.client.module.ModuleManager;
import me.offeex.bloomware.client.module.modules.client.Font;
import me.offeex.bloomware.client.setting.SettingManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Bloomware implements ClientModInitializer {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final String NAME = "Bloomware";
    public static String version;
    public static FontUtil fontUtil = new FontUtil();
    public static FontRenderer Font;
    public static final Logger logger = LogManager.getLogger("bloomware");
    public static EventBus EVENTBUS = new EventBus();
    public static FileManager fileManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static ConfigManager configManager;
    public static SettingManager settingManager;
    public static RotationManager rotationManager;
    public static FriendManager friendManager;
    public static SessionManager sessionManager;
    public static ServerAccountManager serverAccountManager;
    public static MusicManager musicManager;
    public static SpammerManager spammerManager;
    public static ProxyManager proxyManager;
    public static ClickGUI gui;
    public static HudEditor hud;
    public static AccountEditor acc;
    public static Overlay overlay = null;
    public static Screen currentScreen = null;
    public static APIChecker apiChecker;
    public final Object synchronize = new Object();

    public void printLog(String text) {
        synchronized (synchronize) {
            logger.info(text);
        }
    }

    public ModContainer getModContainer() {
        return FabricLoader.getInstance().getModContainer("bloomware").orElse(null);
    }

    @Override
    public void onInitializeClient() {
        printLog("Bloomware started ratting you!");

        System.out.println(
                """

                        ██████╗░██╗░░░░░░█████╗░░█████╗░███╗░░░███╗░██╗░░░░░░░██╗░█████╗░██████╗░███████╗
                        ██╔══██╗██║░░░░░██╔══██╗██╔══██╗████╗░████║░██║░░██╗░░██║██╔══██╗██╔══██╗██╔════╝
                        ██████╦╝██║░░░░░██║░░██║██║░░██║██╔████╔██║░╚██╗████╗██╔╝███████║██████╔╝█████╗░░
                        ██╔══██╗██║░░░░░██║░░██║██║░░██║██║╚██╔╝██║░░████╔═████║░██╔══██║██╔══██╗██╔══╝░░
                        ██████╦╝███████╗╚█████╔╝╚█████╔╝██║░╚═╝░██║░░╚██╔╝░╚██╔╝░██║░░██║██║░░██║███████╗
                        ╚═════╝░╚══════╝░╚════╝░░╚════╝░╚═╝░░░░░╚═╝░░░╚═╝░░░╚═╝░░╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝""");
        fontUtil.generateFonts("assets/fonts/Verdana.ttf", 20);
        version = getModContainer().getMetadata().getVersion().getFriendlyString();
        apiChecker = new APIChecker();
        fileManager = new FileManager();

        fileManager.initClientFiles();

        spammerManager = new SpammerManager();
        settingManager = new SettingManager();
        rotationManager = new RotationManager();
        sessionManager = new SessionManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        proxyManager = new ProxyManager();
        serverAccountManager = new ServerAccountManager();
        serverAccountManager.load();
        proxyManager.loadData();
        friendManager.loadData();
        Bloomware.moduleManager.getModule(Font.class).setEnabled(true);
        printLog(Bloomware.NAME + " finished ratting you!");
    }

    public static void onPostInit() throws IOException {
        EVENTBUS.register(rotationManager);
        EVENTBUS.register(sessionManager);

        gui = new ClickGUI();
        overlay = new Overlay();
        acc = new AccountEditor();
        configManager = new ConfigManager();
        configManager.loadData();
        hud = new HudEditor();
        musicManager = new MusicManager();
        SoundUtil.playSound();
    }
}
