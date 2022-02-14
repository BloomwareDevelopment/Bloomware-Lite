package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {

	private final Packet<?> packet;

	public EventPacket(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public static class Receive extends EventPacket {
		public Receive(Packet<?> packet) {
			super(packet);
		}
	}

	public static class Send extends EventPacket {
		public Send(Packet<?> packet) {
			super(packet);
		}
	}
}