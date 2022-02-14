package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

@Module.Register(name = "Target", description = "Target hud.", category = Module.Category.HUD)
public class Target extends Module {
    private final SettingNumber range = new SettingNumber.Builder("Range").value(30).min(1).max(200).inc(1).setup(this);
    private final SettingBool self = new SettingBool.Builder("Self").value(false).setup(this);
    private PlayerEntity target;
    float temp = 10000;
    boolean found;

    public Target() {
        this.width = 170;
        this.height = 70;
    }

    private Color getColor(float max, float value) {
        double percent = 100 / (max / value);
        if (percent <= 30) return Color.RED;
        if (30 < percent && percent <= 70) return Color.YELLOW;
        if (percent > 70) return Color.GREEN;
        return null;
    }

    private int getWidth(float value) {
        double percent = 100 / (target.getMaxHealth() / value);
        return (int) (170 / 100 * percent);
    }

    private void update() {
        if (mc.world == null || mc.player == null) return;
        if (self.getValue()) {
            target = mc.player;
        } else {
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player.distanceTo(player) < range.getValue() && mc.player.distanceTo(player) < temp && player != mc.player) {
                    target = player;
                    found = true;
                    temp = mc.player.distanceTo(player);
                }
            }
            if (!found) target = null;
            else found = false;
            temp = 10000;
        }
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        if (target != null) {
            DrawableHelper.fill(stack, x, y, x + 170, y + 70, new Color(0, 0, 0, 100).getRGB());
            Bloomware.Font.drawString(stack, target.getEntityName(), x + 10, y + 9, ColorMutable.WHITE);
            Bloomware.Font.drawString(stack, String.format("%.1f", target.getHealth() + target.getAbsorptionAmount())  + " HP", x + 10, y + 20, ColorMutable.WHITE);
            Bloomware.Font.drawString(stack, EntityUtil.getEntityPing(target) + "ms", x + 50, y + 20, ColorMutable.WHITE);
            Bloomware.Font.drawString(stack, String.format("%.1f", mc.player.distanceTo(target)) + "m", x + 90, y + 20, ColorMutable.WHITE);

            int i = 1;
            for (ItemStack item : target.getArmorItems()) {
                RenderUtil.drawItem(item, x + (10 * i) + (i * 10) - 10, y + 35, 1, true);
                i++;
            }

            RenderUtil.drawItem(target.getMainHandStack(), x + 90, y + 35, 1, true);
            RenderUtil.drawItem(target.getOffHandStack(), x + 110, y + 35, 1, true);

            InventoryScreen.drawEntity(x + 150, y + 66, 30, -MathHelper.wrapDegrees(target.prevYaw + (target.getYaw() - target.prevYaw) * mc.getTickDelta()), -target.getPitch(), target);

            DrawableHelper.fill(stack, x, y + height - 2, x + getWidth(target.getAbsorptionAmount() + target.getHealth()) - 9, y + height, getColor(36, 100 / 36f * target.getHealth() + target.getAbsorptionAmount()).getRGB());

            update();
        }
        update();
    }
}
