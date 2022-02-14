package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Module.Register(name = "PlayerModel", description = "Renders interact model", category = Module.Category.HUD)
public class PlayerModel extends Module {
    private final SettingNumber scale = new SettingNumber.Builder("Scale").min(0.1).max(5.0).value(3).inc(0.1).setup(this);

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        this.width = (int) (scale.getValue() * 35);
        this.height = (int) (scale.getValue() * 55);
        InventoryScreen.drawEntity(x + width / 2,
                (int) (y + height - (scale.getValue() * 10)), (int) (scale.getValue() * 20),
                -MathHelper.wrapDegrees(mc.player.prevYaw + (mc.player.getYaw() - mc.player.prevYaw) * mc.getTickDelta()),
                -mc.player.getPitch(), mc.player);
    }
}
