package me.offeex.bloomware.mixins;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static me.offeex.bloomware.Bloomware.mc;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
	@Shadow
	private Channel channel;

	@Shadow
	private void sendImmediately(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener_1) {}

	@Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
	public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) {
		if (this.channel.isOpen() && packet != null && mc.player != null) {
			EventPacket.Receive event = new EventPacket.Receive(packet);
			Bloomware.EVENTBUS.post(event);
			if (event.isCancelled()) {
				callback.cancel();
			}
		}
	}

	@Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
	public void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener_1, CallbackInfo callback) {
		if (packet instanceof ChatMessageC2SPacket pack) {
			if (pack.getChatMessage().startsWith(CommandManager.prefix)) {
				CommandManager.callCommandReturn(pack.getChatMessage());
				callback.cancel();
			}
		}
		EventPacket.Send event = new EventPacket.Send(packet);
		Bloomware.EVENTBUS.post(event);
		if (event.isCancelled()) callback.cancel();
	}
}