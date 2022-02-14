package me.offeex.bloomware.client.module.modules.interact;

import me.offeex.bloomware.client.module.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Module.Register(name = "AirPlace", description = "Allows you to place blocks in air", category = Module.Category.INTERACT)
public class AirPlace extends Module {
    @Override
    public void onTick() {
        if (mc.crosshairTarget instanceof BlockHitResult && mc.player.getMainHandStack().getItem() instanceof BlockItem) {
            BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
            if (mc.world.getBlockState(pos).getBlock() instanceof AirBlock) {
                if (mc.options.keyUse.wasPressed() || mc.options.keyUse.isPressed()) {
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos), Direction.DOWN, pos, false));
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}
