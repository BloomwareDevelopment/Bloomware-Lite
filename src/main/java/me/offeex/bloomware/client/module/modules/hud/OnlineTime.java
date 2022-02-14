package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "OnlineTime", description = "Shows time you are playing", category = Module.Category.HUD)
public class OnlineTime extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        String text = "Online for: " + Bloomware.sessionManager.convertTime(Bloomware.sessionManager.getTimeOnline());
        width = (int) Bloomware.Font.getStringWidth(text) + 6;
        Bloomware.Font.drawString(stack, text, x + 3, y + 2, ColorUtils.getHudColor());
    }
}
