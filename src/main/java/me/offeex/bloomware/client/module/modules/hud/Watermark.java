package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "Watermark", description = "Shows logo", category = Module.Category.HUD)
public class Watermark extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth(Bloomware.NAME + " v" + Bloomware.version) + 6;
        Bloomware.Font.drawString(stack, Bloomware.NAME + " v" + Bloomware.version, x + 3, y + 2, ColorUtils.getHudColor());
    }
}
