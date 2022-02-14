package me.offeex.bloomware.client.module.modules.chat;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.ArrayList;
import java.util.Arrays;

@Module.Register(name = "AntiSpam", description = "Cleans your chat.", category = Module.Category.CHAT)
public class AntiSpam extends Module {
    private final ArrayList<String> forbidden = new ArrayList<>(Arrays.asList("https:", "http:", ".com", ".ru", ".cc", ".gg", ".top", ".wtf", ".xyz", ".org", ".net"));
    private final SettingBool links = new SettingBool.Builder("Links").value(true).setup(this);
    private final SettingBool nWords = new SettingBool.Builder("NWords").value(true).setup(this);

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof GameMessageS2CPacket packet) {
            if (links.getValue()) {
                for (String str : forbidden) {
                    if (packet.getMessage().asString().contains(str)) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
            if (nWords.getValue() && !event.isCancelled()) {
                if (packet.getMessage().getString().contains("nigg")) event.setCancelled(true);
            }
        }
    }
}
