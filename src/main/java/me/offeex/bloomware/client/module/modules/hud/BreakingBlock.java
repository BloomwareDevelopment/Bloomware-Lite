package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.mixins.accessors.IClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "BreakingBlock", description = "Shows percentage progress of breaking block.", category = Module.Category.HUD)
public class BreakingBlock extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("Breaking progress: " + String.format("%.0f%%", ((IClientPlayerInteractionManager) mc.interactionManager).getBreakingProgress() * 100)) + 6;
        Bloomware.Font.drawString(stack, "Breaking progress: " + String.format("%.0f%%", ((IClientPlayerInteractionManager) mc.interactionManager).getBreakingProgress() * 100), x + 3, y + 2, ColorUtils.getHudColor());
    }
}
