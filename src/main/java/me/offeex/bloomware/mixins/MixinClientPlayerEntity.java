package me.offeex.bloomware.mixins;

import com.mojang.authlib.GameProfile;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.*;
import me.offeex.bloomware.client.module.modules.motion.NoSlow;
import me.offeex.bloomware.client.module.modules.world.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    @Shadow
    protected abstract void autoJump(float f, float g);

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("HEAD"), method = "move", cancellable = true)
    public void move(final MovementType movementType, Vec3d vec3d, final CallbackInfo info) {
        EventPlayerMove event = new EventPlayerMove(movementType, vec3d);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) info.cancel();
        else if (movementType != MovementType.SELF || !vec3d.equals(event.getVec3d())) {
            double x = this.getX();
            double z = this.getZ();
            super.move(event.getType(), event.getVec3d());
            this.autoJump((float) (this.getX() - x), (float) (this.getZ() - z));
            info.cancel();
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets(CallbackInfo info) {
        EventSendMovementPackets event = new EventSendMovementPackets();
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) info.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (mc.player != null && mc.world != null) {
            Bloomware.moduleManager.onTick();
            EventTick event = new EventTick();
            Bloomware.EVENTBUS.post(event);
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void pushOutOfBlocks(double x, double z, CallbackInfo ci) {
        EventPlayerPush event = new EventPlayerPush();
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.closeHandledScreen()V"))
    public void closeContainerOverride(ClientPlayerEntity player) {
        EventPortalGUI event = new EventPortalGUI();
        Bloomware.EVENTBUS.post(event);
        if (!event.isCancelled()) player.closeHandledScreen();
    }

    @Inject(method = "updateNausea", at = @At("HEAD"), cancellable = true)
    public void shouldRenderNausea(CallbackInfo ci) {
        NoRender module = Bloomware.moduleManager.getModule(NoRender.class);
        if (module.nausea.getValue() && module.isEnabled()) ci.cancel();
    }

    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "net/minecraft/client/MinecraftClient.setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    public void nullScreenOverride(MinecraftClient mc, Screen screen) {
        EventPortalGUI event = new EventPortalGUI();
        Bloomware.EVENTBUS.post(event);
        if (!event.isCancelled()) mc.setScreen(screen);
    }

    @Inject(method = "shouldSlowDown", at = @At(value = "HEAD"), cancellable = true)
    public void shouldSlowDown(CallbackInfoReturnable<Boolean> cir) {
        EventSlowDown event = new EventSlowDown();
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) cir.cancel();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean onUsingItemCheck(ClientPlayerEntity player) {
        NoSlow module = Bloomware.moduleManager.getModule(NoSlow.class);
        if (module.isEnabled() && module.items.getValue()) return false;
        return player.isUsingItem();
    }
}