package me.offeex.bloomware.mixins.accessors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.net.Proxy;

@Mixin(MinecraftClient.class)
public interface IMinecraftClient {

    @Accessor("currentFps")
    static int getCurrentFps() {
        return 0;
    }

    @Accessor("networkProxy")
    Proxy getProxy();

    @Accessor("itemUseCooldown")
    int getItemUseCooldown();

    @Accessor("renderTickCounter")
    RenderTickCounter getRenderTickCounter();

    @Mutable @Accessor("session")
    void setSession(Session session);

    @Accessor("itemUseCooldown")
    void setItemUseCooldown(int cooldown);

}
