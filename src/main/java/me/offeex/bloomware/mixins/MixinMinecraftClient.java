package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventJoinWorld;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.client.module.modules.screen.Freecam;
import me.offeex.bloomware.client.module.modules.extension.StreamerMode;
import me.offeex.bloomware.client.module.modules.packet.PacketCancel;
import me.offeex.bloomware.client.module.modules.extension.UnfocusedCPU;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow
    private boolean windowFocused;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Final
    private Session session;

    @Inject(at = @At("TAIL"), method = "getWindowTitle", cancellable = true)
    public void getWindowTitle(CallbackInfoReturnable<String> cir) {
        StreamerMode module = Bloomware.moduleManager.getModule(StreamerMode.class);
        cir.setReturnValue(module.isEnabled() ? Bloomware.NAME + " v" + Bloomware.version + " | Streamer Mode" : Bloomware.NAME + " v" + Bloomware.version);
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo info) {
        EventOpenScreen event = new EventOpenScreen(screen);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "scheduleStop")
    public void stop(CallbackInfo ci) {
        Freecam freecam = Bloomware.moduleManager.getModule(Freecam.class);
        PacketCancel packetCancel = Bloomware.moduleManager.getModule(PacketCancel.class);
        if (freecam.isEnabled()) freecam.disable();
        if (packetCancel.isEnabled()) packetCancel.disable();
        Bloomware.configManager.saveData();
        try {
            Bloomware.friendManager.saveData();
            Bloomware.spammerManager.saveMessages();
            Bloomware.proxyManager.saveData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void getFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (Bloomware.moduleManager.getModule("UnfocusedCPU").isEnabled() && !this.windowFocused)
            cir.setReturnValue(((int) ((UnfocusedCPU) Bloomware.moduleManager.getModule("UnfocusedCPU")).limit.getValue()));
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V", shift = At.Shift.BEFORE))
    public void init(RunArgs args, CallbackInfo callback) {
        try {
            Bloomware.onPostInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Ljava/io/InputStream;Ljava/io/InputStream;)V"))
    public void setAlternativeWindowIcon(Window window, InputStream inputStream1, InputStream inputStream2) throws IOException {
        window.setIcon(Bloomware.class.getResourceAsStream("/assets/bloomware/elements/tray/icon16x16.png"), Bloomware.class.getResourceAsStream("/assets/bloomware/elements/tray/icon32x32.png"));
    }

    @Inject(method = "joinWorld", at = @At("HEAD"), cancellable = true)
    public void joinWorld(ClientWorld clientWorld, CallbackInfo ci) {
        EventJoinWorld event = new EventJoinWorld();
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
