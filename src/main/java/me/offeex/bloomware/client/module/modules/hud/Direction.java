package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "Direction", description = "Shows direction you're looking", category = Module.Category.HUD)
public class Direction extends Module {
    @Override public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("Direction: " + (mc.player.getHorizontalFacing().getDirection() == net.minecraft.util.math.Direction.AxisDirection.POSITIVE ? "+" : "-") + mc.player.getHorizontalFacing().getAxis().asString()) + 6;
        Bloomware.Font.drawString(stack, "Direction: " + (mc.player.getHorizontalFacing().getDirection() == net.minecraft.util.math.Direction.AxisDirection.POSITIVE ? "+" : "-") + mc.player.getHorizontalFacing().getAxis().asString(), x + 3, y + 2, ColorUtils.getHudColor());
    }
}
