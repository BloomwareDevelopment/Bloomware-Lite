package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.screen.Freecam;
import me.offeex.bloomware.client.module.modules.screen.CameraClip;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    private boolean thirdPerson;
    @Unique
    private float tickDelta;

    @Shadow
    protected abstract double clipToSpace(double desiredCameraDistance);

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
        if (Bloomware.moduleManager.getModule(CameraClip.class).isEnabled()) {
            info.setReturnValue((Bloomware.moduleManager.getModule(CameraClip.class)).cameraDistance.getValue());
        }
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void onUpdateTail(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        Freecam module = Bloomware.moduleManager.getModule(Freecam.class);
        if (module.isEnabled())
            this.thirdPerson = true;
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdateHead(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        this.tickDelta = tickDelta;
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void onSetRot(Args args) {
        Freecam module = Bloomware.moduleManager.getModule(Freecam.class);
        if (module.isEnabled()) {
            args.set(0, module.fakeman.getYaw());
            args.set(1, module.fakeman.getPitch());
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void onSetPos(Args args) {
        Freecam module = Bloomware.moduleManager.getModule(Freecam.class);
        if (module.isEnabled()) {
            args.set(0, module.getX(tickDelta));
            args.set(1, module.getY(tickDelta));
            args.set(2, module.getZ(tickDelta));
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(DDD)V", ordinal = 0))
    private void modifyCameraDistance(Args args) {
        Freecam module = Bloomware.moduleManager.getModule(Freecam.class);
        if (module.isEnabled())
            args.set(0, -clipToSpace(0));
    }
}
