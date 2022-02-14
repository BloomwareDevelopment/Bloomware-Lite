package me.offeex.bloomware.client.module.modules.chat;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

import java.util.ArrayList;
import java.util.Random;

@Module.Register(name = "Spammer", description = "Spams the chat.", category = Module.Category.CHAT)
public class Spammer extends Module {
    private final Random random = new Random();
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Order", "Random").setup(this);
    private final SettingEnum messageType = new SettingEnum.Builder("MessageType").modes("File", "PrioBan").setup(this);
    private final SettingNumber delay = new SettingNumber.Builder("Delay").value(1).min(0).max(100).inc(1).setup(this);

    private final ArrayList<String> file = Bloomware.spammerManager.getMessages();
    private final String[] prioBanMsgs = new String[]{"I love WALDEN", "u mad?", "love walden", "fuck you hause", "hause is a fucking cokesniffer", "don't buy prio, hause is going to spend this money on coke", "money from prio = new doses of coke", "i love building lag machines", "unban terpila"};
    int count = 0;

    @Override
    public void onTick() {
        String[] msgs = switch (messageType.getSelectedStr()) {
            case "File" -> file.toArray(new String[0]);
            case "PrioBan" -> prioBanMsgs;
            default -> null;
        };
        if (msgs.length == 0) return;
        if (mc.player.age % (delay.getValue() * 20) == 0) {
            switch (mode.getSelectedStr()) {
                case "Order" -> {
                    mc.player.sendChatMessage(msgs[count]);
                    count++;
                }
                case "Random" -> mc.player.sendChatMessage(msgs[random.nextInt(msgs.length)]);
            }
        }
        if (count == msgs.length - 1) count = 0;
    }
}
