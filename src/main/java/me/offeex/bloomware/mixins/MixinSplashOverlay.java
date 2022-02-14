package me.offeex.bloomware.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.api.util.ColorMutable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(SplashOverlay.class)
public abstract class MixinSplashOverlay {
    @Shadow
    private float progress;

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Identifier LOGO = new Identifier("bloomware", "icon.png");
        Identifier TEXT = new Identifier("bloomware", "bloomware.png");
        DrawableHelper.fill(matrices, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorMutable.DARK_GRAY.getRGB());
        RenderSystem.setShaderTexture(0, LOGO);
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        DrawableHelper.drawTexture(matrices, mc.getWindow().getScaledWidth() / 2 - calculateIconWidth() / 2, 20, 0, 0, calculateIconWidth(), calculateIconWidth(), calculateIconWidth(), calculateIconWidth());
        RenderSystem.setShaderTexture(0, TEXT);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        DrawableHelper.drawTexture(matrices, mc.getWindow().getScaledWidth() / 2 - getTextWidth() / 2, mc.getWindow().getScaledHeight() - 30 - getTextHeight(), 0, 0, getTextWidth(), getTextHeight(), getTextWidth(), getTextHeight());
        drawProgress(matrices);
    }

    private void drawProgress(MatrixStack stack) {
        int pixels = (int) (mc.getWindow().getScaledWidth() / 100 * progress * 110);
        DrawableHelper.fill(stack, 0, mc.getWindow().getScaledHeight() - 5, pixels, mc.getWindow().getScaledHeight(), ColorMutable.WHITE.getRGB());
    }

    private int calculateIconWidth() {
        return mc.getWindow().getScaledWidth() / 3;
    }

    private int getTextWidth() {
        return (int) (mc.getWindow().getScaledWidth() * 0.8);
    }

    private int getTextHeight() {
        return (int) (mc.getWindow().getScaledHeight() * 0.17);
    }
}
