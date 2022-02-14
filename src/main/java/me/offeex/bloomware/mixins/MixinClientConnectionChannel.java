package me.offeex.bloomware.mixins;

import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.proxy.Proxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(targets = "net/minecraft/network/ClientConnection$1")
public class MixinClientConnectionChannel {
    @Inject(method = "initChannel(Lio/netty/channel/Channel;)V", at = @At("HEAD"))
    public void connect(Channel channel, CallbackInfo cir) {
        Proxy proxy = Bloomware.proxyManager.getCurrentProxy();
        if (proxy != null) {
            if (proxy.type().equals(Proxy.ProxyType.SOCKS4)) channel.pipeline().addFirst(new Socks4ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.username()));
            else channel.pipeline().addFirst(new Socks5ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.username(), proxy.password()));
        }
    }
}
