package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "Ping", description = "Shows your ping", category = Module.Category.HUD)
public class Ping extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        try {
            width = (int) (Bloomware.Font.getStringWidth("Ping: " + playerListEntry.getLatency() + "ms") + 6);
            Bloomware.Font.drawString(stack, "Ping: " + playerListEntry.getLatency() + "ms", x + 3, y + 2, ColorUtils.getHudColor());
        } catch (NullPointerException nullPointerException) {
            width = (int) (Bloomware.Font.getStringWidth("Ping: 0ms") + 6);
            Bloomware.Font.drawString(stack, "Ping: 0ms", x + 3, y + 2, ColorUtils.getHudColor());
        }
    }
}
