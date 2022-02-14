package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.world.NoRender;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantingTableBlockEntityRenderer.class)
public class MixinEnchantingTableBlockEntityRenderer {
    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    public void render(CallbackInfo ci) {
        NoRender module = Bloomware.moduleManager.getModule(NoRender.class);
        if (module.isEnabled() && module.enchantedTable.getValue()) ci.cancel();
    }
}
