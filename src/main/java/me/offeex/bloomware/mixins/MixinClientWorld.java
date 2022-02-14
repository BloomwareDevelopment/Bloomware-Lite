package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventAddedEntity;
import me.offeex.bloomware.api.event.events.EventRemovedEntity;
import me.offeex.bloomware.client.module.modules.visuals.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {
    @Shadow
    @Nullable
    public abstract Entity getEntityById(int id);

    @Inject(method = "addEntityPrivate", at = @At("TAIL"))
    private void onAddEntityPrivate(int id, Entity entity, CallbackInfo info) {
        EventAddedEntity event = new EventAddedEntity(id, entity);
        if (entity != null) Bloomware.EVENTBUS.post(event);
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    private void onRemoveEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo info) {
        EventRemovedEntity event = new EventRemovedEntity(entityId, removalReason);
        if (getEntityById(entityId) != null) Bloomware.EVENTBUS.post(event);
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        Environment module = Bloomware.moduleManager.getModule(Environment.class);
        if (module.isEnabled() && module.sky.isToggled()) cir.setReturnValue(module.skyColor.getColor().getVec3d());
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    private void getCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        Environment module = Bloomware.moduleManager.getModule(Environment.class);
        if (module.isEnabled() && module.clouds.isToggled()) cir.setReturnValue(module.cloudColor.getColor().getVec3d());
    }
}
