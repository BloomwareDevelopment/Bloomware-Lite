package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventBlockBreakingCooldown;
import me.offeex.bloomware.api.event.events.EventBreakBlock;
import me.offeex.bloomware.api.event.events.EventInteractBlock;
import me.offeex.bloomware.client.module.modules.interact.Reach;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Shadow
    private int blockBreakingCooldown;

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", ordinal = 3),
            require = 0)
    private void updateBlockBreakingProgress(ClientPlayerInteractionManager instance, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value);
        Bloomware.EVENTBUS.post(event);
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", ordinal = 4),
            require = 0)
    private void updateBlockBreakingProgress2(ClientPlayerInteractionManager clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value);
        Bloomware.EVENTBUS.post(event);
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Redirect(method = "attackBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I"),
            require = 0)
    private void attackBlock(ClientPlayerInteractionManager clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value);
        Bloomware.EVENTBUS.post(event);
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;shouldCancelInteraction()Z"), cancellable = true)
    private void interactBlock(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> cir) {
        EventInteractBlock event = new EventInteractBlock(clientPlayerEntity, clientWorld, hand, blockHitResult);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) cir.cancel();
        if (event.getActionResult() != null) cir.setReturnValue(event.getActionResult());
    }

    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    private void getReachDistance(CallbackInfoReturnable<Float> cir) {
        Reach module = Bloomware.moduleManager.getModule(Reach.class);
        cir.setReturnValue(module.isEnabled() ? (float) module.amount.getValue() : mc.interactionManager.getCurrentGameMode().isCreative() ? 5.0F : 4.5F);
    }

    @Inject(method = "breakBlock", at = @At("HEAD"))
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        EventBreakBlock event = new EventBreakBlock(mc.world.getBlockState(pos), pos);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) cir.cancel();
    }
}
