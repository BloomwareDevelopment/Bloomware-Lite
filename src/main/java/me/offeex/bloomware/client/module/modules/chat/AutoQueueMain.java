package me.offeex.bloomware.client.module.modules.chat;

import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.hud.Coordinates;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Module.Register(name = "AutoQueueMain", description = "Automatically sends /queue 2b2t-lobby to chat", category = Module.Category.CHAT)
public class AutoQueueMain extends Module {
    private final SettingNumber delay = new SettingNumber.Builder("Delay").min(15).max(120).inc(1).value(45).setup(this);
    private final SettingBool notify = new SettingBool.Builder("Notify").value(true).setup(this);

    private final SimpleDateFormat pattern = new SimpleDateFormat("HH:mm:ss");
    private long time;

    @Override public void onEnable() {
        time = System.currentTimeMillis();
    }

    @Override public void onTick() {
        if ((System.currentTimeMillis() - time) / 1000 >= delay.getValue() && mc.getCurrentServerEntry().address.equals("2b2t.org") && Coordinates.getDimension() == 2) {
            mc.player.sendChatMessage("/queue 2b2t-lobby");
            time = System.currentTimeMillis();
            if (notify.getValue()) CommandManager.addChatMessage("Command /queue 2b2t-lobby was sent in " + pattern.format(Calendar.getInstance().getTime()));
        }
    }
}
