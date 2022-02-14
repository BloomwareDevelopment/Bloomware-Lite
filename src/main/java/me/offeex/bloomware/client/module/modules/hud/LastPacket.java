package me.offeex.bloomware.client.module.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;

@Module.Register(name = "LastPacket", description = "shows time since last packet received", category = Module.Category.HUD)
public class LastPacket extends Module {
    private final SettingBool showPacket = new SettingBool.Builder("ShowPacket").value(false).setup(this);

    private double lastTimePacketReceived;
    private Packet<?> packet;

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        String text = "Since last packet: " + String.format("%.2f", (System.currentTimeMillis() - lastTimePacketReceived) / 1000) + "s " + (showPacket.getValue() ? packet.getClass().getSimpleName() : "");
        width = (int) Bloomware.Font.getStringWidth(text) + 6;
        Bloomware.Font.drawString(stack, text, x + 3, y + 2, ColorUtils.getHudColor());
    }

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        lastTimePacketReceived = System.currentTimeMillis();
        if (showPacket.getValue()) packet = event.getPacket();
    }
}
