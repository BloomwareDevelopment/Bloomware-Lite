package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.AirBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

@Module.Register(name = "BoxHighlight", description = "Highlights block you are looking", category = Module.Category.VISUALS)
public class BoxHighlight extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").selected("Fill").setup(this);
    private final SettingColor color = new SettingColor.Builder("Color").color(new ColorMutable(255, 255, 255, 70)).setup(this);
    private final SettingNumber lineWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(this);

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
//        Entity Render
        if (mc.targetedEntity != null) {
            Box pos = RenderUtil.smoothMovement(mc.targetedEntity, mc.targetedEntity.getBoundingBox());
            if (mode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, pos, color.getColor());
            else RenderUtil.drawOutline(event.matrixStack, pos, color.getColor(), lineWidth.getValue());
        }

//        Block Render
        if (mc.crosshairTarget instanceof BlockHitResult) {
            BlockPos blockPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
            VoxelShape shape = mc.world.getBlockState(blockPos).getOutlineShape(mc.world, blockPos).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (shape.isEmpty()) return;
            Box pos = shape.getBoundingBox();
            if (mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).getBlock() instanceof AirBlock) return;

            if (mode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, pos, color.getColor());
            else RenderUtil.drawOutline(event.matrixStack, pos, color.getColor(), lineWidth.getValue());
        }
    }
}
