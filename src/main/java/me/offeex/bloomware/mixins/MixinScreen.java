package me.offeex.bloomware.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventRender;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static me.offeex.bloomware.Bloomware.mc;

@Mixin(Screen.class)
public class MixinScreen {
    private final Identifier BACKGROUND = new Identifier("bloomware", "background/highwaybg.png");

    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void renderBG(int vOffset, CallbackInfo ci) {
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();
        RenderSystem.setShaderTexture(0, BACKGROUND);
        DrawableHelper.drawTexture(new MatrixStack(), 0, 0, 0, 0, 0, width, height, width, height);
        ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(MatrixStack matrixStack, int i, int j, float f, CallbackInfo ci) {
        EventRender event = new EventRender(matrixStack, i, j, f);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
