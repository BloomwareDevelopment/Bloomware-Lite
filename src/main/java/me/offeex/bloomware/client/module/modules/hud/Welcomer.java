package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.extension.StreamerMode;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "Welcomer", description = "Welcomes you", category = Module.Category.HUD)
public class Welcomer extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        StreamerMode module = (StreamerMode) Bloomware.moduleManager.getModule("StreamerMode");
        if (!module.isEnabled() || (module.isEnabled() && !module.hideNickname.getValue())) {
            width = (int) Bloomware.Font.getStringWidth("Looking cute today, " + mc.getSession().getUsername() + "! :^)") + 6;
            Bloomware.Font.drawString(stack, "Looking cute today, " + mc.getSession().getUsername() + "! :^)", x + 3, y + 2, ColorUtils.getHudColor());
        }
    }
}
