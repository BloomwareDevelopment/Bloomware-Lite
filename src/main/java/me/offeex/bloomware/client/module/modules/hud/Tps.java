package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "TPS", description = "shows current server tps", category = Module.Category.HUD)
public class Tps extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("TPS: " + Bloomware.sessionManager.getTps()) + 6;
        Bloomware.Font.drawString(stack, "TPS: " + Bloomware.sessionManager.getTps(), x + 3, y + 2, ColorUtils.getHudColor());
    }
}
