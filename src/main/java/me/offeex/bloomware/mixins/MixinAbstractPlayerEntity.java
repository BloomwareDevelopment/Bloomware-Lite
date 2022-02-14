package me.offeex.bloomware.mixins;

import com.mojang.authlib.GameProfile;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.visuals.Capes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
abstract class MixinAbstractPlayerEntity extends PlayerEntity {
    private final Identifier DEV_CAPE = new Identifier("bloomware", "/game/devcape.png");
    private final Identifier MAIN_CAPE = new Identifier("bloomware", "/game/maincape.png");

    public MixinAbstractPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        if (Bloomware.moduleManager.getModule(Capes.class).isEnabled()) {
            cir.setReturnValue(MAIN_CAPE);
        }
    }
}