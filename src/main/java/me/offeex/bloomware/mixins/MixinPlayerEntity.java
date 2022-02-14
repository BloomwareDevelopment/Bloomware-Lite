package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventPlayerTravel;
import me.offeex.bloomware.client.module.modules.interact.AntiInteract;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    public MixinPlayerEntity(World worldIn) {
        super(EntityType.PLAYER, worldIn);
    }

    @Redirect(method = "shouldCancelInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSneaking()Z"))
    private boolean shouldCancelInteraction(PlayerEntity instance) {
        AntiInteract module = Bloomware.moduleManager.getModule(AntiInteract.class);
        if (module.blockCheck() && module.isEnabled() && module.forcePlace.getValue())
            return true;
        return instance.isSneaking();
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d vec3d, CallbackInfo ci) {
        EventPlayerTravel event = new EventPlayerTravel();
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) {
            move(MovementType.SELF, getVelocity());
            ci.cancel();
        }
    }
}
