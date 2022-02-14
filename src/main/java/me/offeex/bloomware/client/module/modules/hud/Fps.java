package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.mixins.accessors.IMinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "FPS", description = "Shows the current fps value", category = Module.Category.HUD)
public class Fps extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("FPS: " + IMinecraftClient.getCurrentFps()) + 6;
        Bloomware.Font.drawString(stack, "FPS: " + IMinecraftClient.getCurrentFps(), x + 3, y + 2, ColorUtils.getHudColor());
    }
}
