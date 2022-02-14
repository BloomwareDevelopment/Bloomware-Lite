package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventPlayerPush;
import me.offeex.bloomware.client.module.modules.motion.NoSlow;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class MixinEntity {

    @Redirect(method = "pushAwayFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void onPushAwayFrom(Entity entity, double deltaX, double deltaY, double deltaZ) {
        EventPlayerPush event = new EventPlayerPush(entity, deltaX, deltaZ);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) return;

        entity.setVelocityClient(entity.getVelocity().x + deltaX, entity.getVelocity().y + deltaY, entity.getVelocity().z + deltaZ);
        entity.velocityDirty = event.isVelocityDirty();
    }

    @Redirect(method = "getVelocityMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
    private Block getVelocityMultiplierGetBlockProxy(BlockState blockState) {
        NoSlow module = Bloomware.moduleManager.getModule(NoSlow.class);
        if (blockState.getBlock() == Blocks.SOUL_SAND && module.soulSand.getValue() && module.isEnabled()) return Blocks.STONE;
        return blockState.getBlock();
    }
}
