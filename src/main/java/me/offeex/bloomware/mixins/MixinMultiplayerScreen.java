package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.client.Gui;
import me.offeex.bloomware.client.module.modules.client.Hud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.offeex.bloomware.Bloomware.currentScreen;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen extends Screen {
    @Shadow
    private ButtonWidget buttonEdit;

    @Shadow
    private ButtonWidget buttonJoin;

    @Shadow
    private ButtonWidget buttonDelete;

    @Shadow protected MultiplayerServerListWidget serverListWidget;

    @Shadow @Final private Screen parent;

    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(method = "connect*", at = @At("TAIL"))
    public void connect(CallbackInfo ci) {
        Bloomware.sessionManager.reset();
        Bloomware.sessionManager.start();
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(5, 5, 50, 20, Text.of("Proxy"), (button) -> {
            System.out.println("Hello!");
        }));
    }

    @Inject(method = "connect*", at = @At("HEAD"), cancellable = true)
    public void onConnect(CallbackInfo ci) {
        if (currentScreen != null) ci.cancel();
    }

    @Inject(method = "select", at = @At("HEAD"), cancellable = true)
    public void onSelect(MultiplayerServerListWidget.Entry entry, CallbackInfo ci) {
        if (currentScreen != null) ci.cancel();
    }

    @Inject(method = "refresh", at = @At("HEAD"), cancellable = true)
    public void onRefresh(CallbackInfo ci) {
        if (currentScreen != null) ci.cancel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (currentScreen == Bloomware.gui || currentScreen == Bloomware.hud) {
            currentScreen.render(matrices, mouseX, mouseY, delta);
            serverListWidget.setSelected(null);
            buttonEdit.active = false;
            buttonDelete.active = false;
            buttonJoin.active = false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentScreen != null) currentScreen.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (currentScreen != null) currentScreen.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == Bloomware.moduleManager.getModule(Gui.class).getKey()) currentScreen = Bloomware.gui;
        else if (keyCode == Bloomware.moduleManager.getModule(Hud.class).getKey()) currentScreen = Bloomware.hud;
        else if (keyCode == GLFW.GLFW_KEY_ESCAPE && currentScreen != null) {
            currentScreen = null;
            cir.cancel();
        }
        if (currentScreen != null) currentScreen.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean charTyped(char c, int i) {
        if (currentScreen != null) currentScreen.charTyped(c, i);
        return super.charTyped(c, i);
    }
}
