package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

@Module.Register(name = "InventoryViewer", description = "Allows you to see your inventory.", category = Module.Category.HUD)
public class InventoryViewer extends Module {
    private final SettingBool background = new SettingBool.Builder("Background").value(true).setup(this);

    public InventoryViewer() {
        this.width = 153;
        this.height = 51;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        int offsetX = 0, offsetY = 0;
        for (int slot = 9; slot < 36; slot++) {
            RenderUtil.drawItem(mc.player.getInventory().getStack(slot), this.x + (offsetX * 17), this.y + (offsetY * 17), 1, false);
            offsetX++;
            if ((slot + 1) % 9 == 0) {
                offsetY++;
                offsetX = 0;
            }
        }

        if (background.getValue() && mc.currentScreen != Bloomware.hud) DrawableHelper.fill(stack, this.x, this.y, this.x + 153, this.y + 51, new Color(0, 0, 0, 100).getRGB());
    }
}
