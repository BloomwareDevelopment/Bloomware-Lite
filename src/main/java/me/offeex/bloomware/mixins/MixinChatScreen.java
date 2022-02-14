package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.ModuleManager;
import me.offeex.bloomware.client.module.modules.chat.BetterChat;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class MixinChatScreen {
    @Shadow protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setMaxLength(I)V", shift = At.Shift.AFTER))
    private void changeMaxLength(CallbackInfo ci) {
        BetterChat module = Bloomware.moduleManager.getModule(BetterChat.class);
        if (module.isEnabled() && module.infiniteTextField.getValue()) chatField.setMaxLength(Integer.MAX_VALUE);
    }

    @Inject(method = "onChatFieldUpdate", at = @At("HEAD"))
    private void onChatFieldUpdate(String string, CallbackInfo ci) {
        BetterChat module = Bloomware.moduleManager.getModule(BetterChat.class);
        chatField.setMaxLength(module.infiniteChat.getValue() && module.isEnabled() ? Integer.MAX_VALUE : 256);
    }
}
