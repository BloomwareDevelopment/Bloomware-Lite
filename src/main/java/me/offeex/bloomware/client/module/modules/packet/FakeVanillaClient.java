package me.offeex.bloomware.client.module.modules.packet;

import com.google.common.eventbus.Subscribe;
import io.netty.buffer.Unpooled;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import java.nio.charset.StandardCharsets;

@Module.Register(name = "FakeVanillaClient", description = "Says server that you are using vanilla Minecraft client", category = Module.Category.PACKET)
public class FakeVanillaClient extends Module {
    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof CustomPayloadC2SPacket packet) {
            if (packet.getChannel().equals(CustomPayloadC2SPacket.BRAND)) packet.write(new PacketByteBuf(Unpooled.buffer()).writeString("vanilla"));
            event.setCancelled(packet.getData().toString(StandardCharsets.UTF_8).contains("fabric"));
        }
    }
}
