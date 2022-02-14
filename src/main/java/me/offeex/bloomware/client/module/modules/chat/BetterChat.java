package me.offeex.bloomware.client.module.modules.chat;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.extension.Notifications;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.mixins.accessors.IChatMessageC2SPacket;
import me.offeex.bloomware.mixins.accessors.IGameMessageS2CPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Module.Register(name = "BetterChat", description = "Improves your chat.", category = Module.Category.CHAT)
public class BetterChat extends Module {
    private final ArrayList<String> forbiddenSymbols = new ArrayList<>(Arrays.asList(".", ";", ">", "$", "/"));
    private final SettingBool greenText = new SettingBool.Builder("GreenText").value(false).setup(this);
    private final SettingBool chatSuffix = new SettingBool.Builder("ChatSuffix").value(true).setup(this);
    private final SettingBool timestamp = new SettingBool.Builder("Timestamps").value(true).setup(this);
    private final SettingBool antiCoordLeak = new SettingBool.Builder("AntiCoordLeak").value(true).setup(this);
    public final SettingBool infiniteTextField = new SettingBool.Builder("InfiniteTextField").value(false).setup(this);
    private final SettingBool antiLog4j = new SettingBool.Builder("AntiLog4j").value(true).setup(this);
    private final SettingGroup mentions = new SettingGroup.Builder("Mentions").toggleable(true).setup(this);
    private final SettingBool sound = new SettingBool.Builder("Sound").value(true).setup(mentions);
    private final SettingBool notification = new SettingBool.Builder("Notification").value(false).setup(mentions);
    public final SettingBool infiniteChat = new SettingBool.Builder("InfiniteChat").value(true).setup(this);

    private String msg = "";

    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof ChatMessageC2SPacket packet && !packet.getChatMessage().startsWith("/")) {
            String message = "";
            if (!forbiddenSymbols.contains(packet.getChatMessage().substring(0, 0)) && greenText.getValue() && !packet.getChatMessage().startsWith("/")) {
                message += "> " + packet.getChatMessage();
            }
            if (chatSuffix.getValue()) {
                message += greenText.getValue() ? " <" + Bloomware.NAME + ">" : packet.getChatMessage() + " <" + Bloomware.NAME + ">";
            }
            if (antiCoordLeak.getValue() && isCoords(message)) {
                event.setCancelled(true);
                CommandManager.addChatMessage("This message may contain coordinates. Sending was canceled.");
            }
            ((IChatMessageC2SPacket) packet).setChatMessage(message);
            msg = message;
        }
    }

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof GameMessageS2CPacket packet) {
            if (packet.getMessage().getString().contains("jndi:ldap") && antiLog4j.getValue())
                event.setCancelled(true);

            Text message = packet.getMessage().shallowCopy();
            if (timestamp.getValue()) {
                message = new LiteralText("\u00A77[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] ").append(message);
                ((IGameMessageS2CPacket) packet).setMessage(message);
            }
            if (mentions.isToggled() && message.getString().toLowerCase(Locale.ROOT).contains(mc.getSession().getUsername().toLowerCase(Locale.ROOT)) && !message.getString().contains(msg)) {
                if (sound.getValue())
                    mc.world.playSound(mc.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1f, 1f, true);
                if (notification.getValue())
                    Bloomware.moduleManager.getModule(Notifications.class).showNotification("You just have been mentioned in chat!");
            }
        }
    }

    private boolean isCoords(String message) {
        short coord = 0;
        for (String word : message.split(" ")) if (NumberUtils.isParsable(word)) coord++;
        return coord >= 2;
    }
}
