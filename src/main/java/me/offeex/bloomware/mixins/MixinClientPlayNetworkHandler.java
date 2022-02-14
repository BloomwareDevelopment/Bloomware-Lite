package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventExplosionVelocity;
import me.offeex.bloomware.api.event.events.EventPlayerVelocity;
import me.offeex.bloomware.client.module.modules.motion.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "onExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    public void onExplosion(ClientPlayerEntity instance, Vec3d vec3d) {
        Velocity module = Bloomware.moduleManager.getModule(Velocity.class);
        EventExplosionVelocity event = new EventExplosionVelocity();
        Bloomware.EVENTBUS.post(event);
        if (module.isEnabled() && module.explosions.getValue() && instance.getId() == client.player.getId())
            instance.addVelocity(vec3d.getX() * event.getMultX(), vec3d.getY() * event.getMultY(), vec3d.getZ() * event.getMultZ());
        else
            instance.addVelocity(vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

    @Redirect(method = "onEntityVelocityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V"))
    public void onVelocityUpdate(Entity instance, double x, double y, double z) {
        Velocity module = Bloomware.moduleManager.getModule(Velocity.class);
        EventPlayerVelocity event = new EventPlayerVelocity(instance);
        Bloomware.EVENTBUS.post(event);
        if (module.isEnabled() && instance.getId() == client.player.getId())
            instance.setVelocity(x * event.getMultX(), y * event.getMultY(), z * event.getMultZ());
        else
            instance.setVelocity(x, y, z);
    }
}
