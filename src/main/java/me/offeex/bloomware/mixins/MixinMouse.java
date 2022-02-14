package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.screen.Freecam;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public class MixinMouse {
    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    public void onchangeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
        Freecam module = Bloomware.moduleManager.getModule(Freecam.class);
        AbstractClientPlayerEntity entity = module.isEnabled() ? module.fakeman : player;

        float f = (float) cursorDeltaY * 0.15f;
        float g = (float) cursorDeltaX * 0.15f;
        entity.setPitch(entity.getPitch() + f);
        entity.setYaw(entity.getYaw() + g);
        entity.setPitch(MathHelper.clamp(entity.getPitch(), -90.0f, 90.0f));
        entity.prevPitch += f;
        entity.prevYaw += g;
        entity.prevPitch = MathHelper.clamp(entity.prevPitch, -90.0f, 90.0f);
        if (entity.getVehicle() != null) entity.getVehicle().onPassengerLookAround(entity);
    }
}
