package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.mainmenu.CustomMainMenu;
import me.offeex.bloomware.client.module.modules.client.Gui;
import me.offeex.bloomware.client.module.modules.client.Hud;
import me.offeex.bloomware.client.module.modules.client.MainMenu;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.offeex.bloomware.Bloomware.currentScreen;
import static me.offeex.bloomware.Bloomware.mc;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Text text) {
        super(Text.of("BloomwareTitleScreen"));
    }

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        if (Bloomware.moduleManager.getModule(MainMenu.class).isEnabled())
            Bloomware.mc.setScreen(new CustomMainMenu());
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
//        TODO: altmanager
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawStringWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public void onDrawStrings(MatrixStack matrixStack, TextRenderer textRenderer, String s, int x, int y, int color) {
        if (currentScreen == null) textRenderer.drawWithShadow(matrixStack, s, x, y, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    public void onFill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        if (currentScreen == null) DrawableHelper.fill(matrices, x1, y1, x2, y2, color);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        mc.textRenderer.drawWithShadow(matrices, Formatting.DARK_PURPLE + Bloomware.NAME + Formatting.WHITE + " v" + Bloomware.version + " made by OffeeX, DiOnFire & Rikonardo", 5, 5, 0);
        if (currentScreen == Bloomware.gui || currentScreen == Bloomware.hud) currentScreen.render(matrices, mouseX, mouseY, delta);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (currentScreen != null) {
            currentScreen.mouseClicked(mouseX, mouseY, button);
            cir.cancel();
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (currentScreen != null) currentScreen.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Bloomware.moduleManager.getModule(Gui.class).getKey()) currentScreen = Bloomware.gui;
        else if (keyCode == Bloomware.moduleManager.getModule(Hud.class).getKey()) currentScreen = Bloomware.hud;
        else if (keyCode == GLFW.GLFW_KEY_ESCAPE) currentScreen = null;

        if (currentScreen != null) currentScreen.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean charTyped(char c, int i) {
        if (currentScreen != null) currentScreen.charTyped(c, i);
        return super.charTyped(c, i);
    }
}
