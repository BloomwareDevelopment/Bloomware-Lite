package me.offeex.bloomware.mixins.accessors;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface IClientPlayerInteractionManager {
    @Accessor("currentBreakingProgress")
    float getBreakingProgress();

    @Accessor("currentBreakingPos")
    BlockPos getCurrentBreakingBlockPos();

    @Accessor("blockBreakingCooldown")
    int getBlockBreakingCooldown();
}
