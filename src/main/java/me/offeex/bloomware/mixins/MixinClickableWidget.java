package me.offeex.bloomware.mixins;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.offeex.bloomware.Bloomware.currentScreen;

@Mixin(ClickableWidget.class)
public class MixinClickableWidget {
    @Inject(method = "isHovered", at = @At("HEAD"), cancellable = true)
    public void onRenderButton(CallbackInfoReturnable<Boolean> cir) {
        if (currentScreen != null) cir.setReturnValue(false);
    }
}
