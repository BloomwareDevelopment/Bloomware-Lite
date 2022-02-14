package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "CurrentServer", description = "Shows current server you playing", category = Module.Category.HUD)
public class Server extends Module {
    String server = "s";

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        if (mc.getCurrentServerEntry() != null) {
            server = mc.getCurrentServerEntry().address;
        } else if (mc.isInSingleplayer()) {
            server = "SinglePlayer";
        } else {
            server = "null";
        }

        width = (int) Bloomware.Font.getStringWidth("Server: " + server) + 6;
        Bloomware.Font.drawString(stack, "Server: " + server, x + 3, y + 2, ColorUtils.getHudColor());
    }
}
