package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.api.util.RotationUtil;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "YawPitch", description = "Shows your yaw & pitch", category = Module.Category.HUD)
public class YawPitch extends Module {
    public YawPitch() {
        this.height = 32;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        String yawText = "Yaw: " + String.format("%.1f", RotationUtil.fixYaw(mc.player.getYaw()));
        String pitchText = "Pitch: " + String.format("%.1f", mc.player.getPitch());
        width = (int)(Math.max(Bloomware.Font.getStringWidth(yawText), Bloomware.Font.getStringWidth(pitchText))) + 6;
        Bloomware.Font.drawString(stack, "Yaw: " + String.format("%.1f", RotationUtil.fixYaw(mc.player.getYaw())), x + 3, y + 2, ColorUtils.getHudColor());
        Bloomware.Font.drawString(stack, "Pitch: " + String.format("%.1f", mc.player.getPitch()), x + 3, y + 18, ColorUtils.getHudColor());
    }
}
