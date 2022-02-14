package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "MemoryUsage", description = "Shows how many RAM minecraft uses", category = Module.Category.HUD)
public class MemoryUsage extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        width = (int) Bloomware.Font.getStringWidth("Memory: " + String.format("% 2d%% %03d/%03dMB", (totalMemory - freeMemory) * 100L / maxMemory, toMiB(totalMemory - freeMemory), toMiB(maxMemory))) + 6;
        Bloomware.Font.drawString(stack, "Memory: " + String.format("% 2d%% %03d/%03dMB", (totalMemory - freeMemory) * 100L / maxMemory, toMiB(totalMemory - freeMemory), toMiB(maxMemory)), x + 3, y + 2, ColorUtils.getHudColor());
    }

    private long toMiB(long bytes) {
        return bytes / 1024L / 1024L;
    }
}
