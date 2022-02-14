package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.client.module.modules.interact.Reach;
import me.offeex.bloomware.client.module.modules.world.NoRender;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void renderHand(MatrixStack matrixStack, Camera camera, float tickdelta, CallbackInfo info) {
        EventWorldRender event = new EventWorldRender(matrixStack, tickdelta);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) info.cancel();
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void bobViewWhenHurt(MatrixStack matrixStack, float f, CallbackInfo ci) {
        NoRender module = Bloomware.moduleManager.getModule(NoRender.class);
        if (module.isEnabled() && module.hurtCamera.getValue()) ci.cancel();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void bobView(MatrixStack matrixStack, float f, CallbackInfo ci) {
        NoRender module = Bloomware.moduleManager.getModule(NoRender.class);
        if (module.isEnabled() && module.bob.getValue()) ci.cancel();
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3))
    private double updateTargetedEntityModifySurvivalReach(double d) {
        Reach reach = Bloomware.moduleManager.getModule(Reach.class);
        return reach.isEnabled() ? reach.amount.getValue() : d;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        Reach reach = Bloomware.moduleManager.getModule(Reach.class);
        return reach.isEnabled() ? Math.pow(reach.amount.getValue(), 2) : d;
    }
}