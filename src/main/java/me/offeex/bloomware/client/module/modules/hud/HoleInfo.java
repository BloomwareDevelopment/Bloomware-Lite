package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.api.util.HoleUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Module.Register(name = "HoleInfo", description = "Shows hole info", category = Module.Category.HUD)
public class HoleInfo extends Module {
    public HoleInfo() {
        this.width = 50;
        this.height = 50;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        if (!HoleUtil.isHole(mc.player.getBlockPos()).equals(HoleUtil.HoleType.UNSAFE)) {
            RenderUtil.drawItem(new ItemStack(mc.world.getBlockState(mc.player.getBlockPos().west()).getBlock()), x + 3, y + 20, 1, false);
            RenderUtil.drawItem(new ItemStack(mc.world.getBlockState(mc.player.getBlockPos().north()).getBlock()), x + 20, y + 1, 1, false);
            RenderUtil.drawItem(new ItemStack(mc.world.getBlockState(mc.player.getBlockPos().east()).getBlock()), x + 38, y + 20, 1, false);
            RenderUtil.drawItem(new ItemStack(mc.world.getBlockState(mc.player.getBlockPos().south()).getBlock()), x + 20, y + 37, 1, false);
        }
    }
}
