package me.offeex.bloomware.mixins;

import me.offeex.bloomware.mixins.accessors.ISoundManager;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(GameMenuScreen.class)
public abstract class MixinGameMenuScreen extends Screen {
    protected MixinGameMenuScreen(Text text) {
        super(text);
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void addCustomButton(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(10, 10, 150, 20, Text.of("Reload sound system"), (buttonWidgetx) -> {
            ((ISoundManager)mc.getSoundManager()).getSoundSystem().reloadSounds();
        }));
    }
}
