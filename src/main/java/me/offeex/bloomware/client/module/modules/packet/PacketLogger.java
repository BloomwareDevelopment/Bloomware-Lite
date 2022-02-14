package me.offeex.bloomware.client.module.modules.packet;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;

@Module.Register(name = "PacketLogger", description = "", category = Module.Category.PACKET)
public class PacketLogger extends Module {
    private final SettingBool receive = new SettingBool.Builder("ViewReceived", "view_received").value(true).setup(this);
    private final SettingBool send = new SettingBool.Builder("ViewSent", "view_sent").value(true).setup(this);

    @Subscribe
    private void onPacket(EventPacket event) {
        if (event instanceof EventPacket.Receive && receive.getValue()) CommandManager.addChatMessage("Received packet -> " + event.getPacket().toString());
        if (event instanceof EventPacket.Send && send.getValue()) CommandManager.addChatMessage("Sent packet -> " + event.getPacket().toString());
    }
}
