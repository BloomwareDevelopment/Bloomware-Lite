package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

@Module.Register(name = "ModuleNotifier", description = "Adds the window with the notifications about toggling modules.", category = Module.Category.HUD)
public class ModuleNotifier extends Module {
    private long time;
    private String message = "";

    public void setMessage(String msg) {
        time = System.currentTimeMillis();
        message = msg;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        if (System.currentTimeMillis() - time < 2000) {
            width = (int) (Bloomware.Font.getStringWidth(message)) + 6;
            Bloomware.Font.drawString(stack, message, x + 3, y + 2, ColorUtils.getHudColor());
        }
    }
}
