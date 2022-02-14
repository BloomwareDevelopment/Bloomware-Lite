package me.offeex.bloomware.mixins.accessors;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTickCounter.class)
public interface IRenderTickCounter {
    @Accessor("tickTime")
    float getTickTime();

    @Mutable @Accessor("tickTime")
    void setTickTime(float ticks);
}
