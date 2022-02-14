package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.api.util.MovementUtil;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "Speedometer", description = "Your speed.", category = Module.Category.HUD)
public class Speedometer extends Module {

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("Speed: " + String.format("%.1f", MovementUtil.getPlayerSpeed()) + 6);
        Bloomware.Font.drawString(stack, "Speed: " + String.format("%.1f", MovementUtil.getPlayerSpeed()), x + 3, y + 2, ColorUtils.getHudColor());
    }
}
