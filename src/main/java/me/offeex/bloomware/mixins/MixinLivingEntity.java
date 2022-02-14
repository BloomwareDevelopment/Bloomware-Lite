package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.motion.NoSlow;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "applyClimbingSpeed", at = @At("HEAD"), cancellable = true)
    private void applyClimbingSpeed(Vec3d motion, CallbackInfoReturnable<Vec3d> cir) {
        if (this.getClass().getName().equals("ClientPlayerEntity")) {
            NoSlow module = Bloomware.moduleManager.getModule(NoSlow.class);
            if (module.isEnabled() && module.ladders.getValue())
                cir.setReturnValue(motion);
        }
    }
}
