package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.visuals.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class MixinBiomeColors {
    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private static void getWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Environment module = Bloomware.moduleManager.getModule(Environment.class);
        if (module.isEnabled() && module.water.isToggled()) cir.setReturnValue(module.waterColor.getColor().getRGB());
    }

    @Inject(method = "getGrassColor", at = @At("HEAD"), cancellable = true)
    private static void getGrassBlock(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Environment module = Bloomware.moduleManager.getModule(Environment.class);
        if (module.isEnabled() && module.grass.isToggled()) cir.setReturnValue(module.grassColor.getColor().getRGB());
    }

    @Inject(method = "getFoliageColor", at = @At("HEAD"), cancellable = true)
    private static void onGetFoliageColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Environment module = Bloomware.moduleManager.getModule(Environment.class);
        if (module.isEnabled() && module.leaves.isToggled()) cir.setReturnValue(module.leavesColor.getColor().getRGB());
    }
}
