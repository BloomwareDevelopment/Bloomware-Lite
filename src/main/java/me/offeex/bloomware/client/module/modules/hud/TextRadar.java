package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

@Module.Register(name = "TextRadar", description = "Shows players around you", category = Module.Category.HUD)
public class TextRadar extends Module {
    private final SettingBool showHealth = new SettingBool.Builder("Health").value(true).setup(this);
    private final SettingBool showDistance = new SettingBool.Builder("Distance").value(true).setup(this);
    private final SettingNumber range = new SettingNumber.Builder("Range").value(100).min(1).max(200).inc(1).setup(this);
    private final SettingNumber maxCount = new SettingNumber.Builder("MaxCount").value(15).min(1).max(100).inc(1).setup(this);

    public TextRadar() {
        this.width = 100;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        int offset = 0;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (mc.player.distanceTo(player) <= range.getValue() && offset <= maxCount.getValue() && player != mc.player) {
                String string = "";
                if (showHealth.getValue()) string += String.format("%.1f", player.getHealth()) + " : ";
                string += player.getEntityName();
                if (showDistance.getValue()) string += " : " + String.format("%.1f", mc.player.distanceTo(player));
                width = (int) Bloomware.Font.getStringWidth(string);
                Bloomware.Font.drawString(stack, string, x + 3, y + (16 * offset), ColorUtils.getHudColor());
                offset++;
            }
        }
    }
}
