package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventKeyHold;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.api.event.events.EventKeyRelease;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Shadow private boolean repeatEvents;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKeyEvent(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (mc.currentScreen != null) return;
        if (action == 2) action = 1;
        switch (action) {
            case 0 -> {
                EventKeyRelease event = new EventKeyRelease(key, scanCode);
                Bloomware.EVENTBUS.post(event);
                if (event.isCancelled()) callbackInfo.cancel();
            }
            case 1 -> {
                EventKeyPress event = new EventKeyPress(key, scanCode);
                Bloomware.moduleManager.getModules().stream().filter(m -> m.getKey() == key).forEach(Module::toggle);
                Bloomware.EVENTBUS.post(event);
                if (event.isCancelled()) callbackInfo.cancel();
            }
//                EventKeyHold event = new EventKeyHold(key, scanCode);
//                Bloomware.EVENTBUS.post(event);
//                if (event.isCancelled()) callbackInfo.cancel();
        }
    }
}