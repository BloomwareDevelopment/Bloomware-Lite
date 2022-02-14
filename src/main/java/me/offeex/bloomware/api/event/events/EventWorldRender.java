package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class EventWorldRender extends Event {

	public final float tickdelta;
	public MatrixStack matrixStack;

	public EventWorldRender(MatrixStack matrixStack, float partialTicks) {
		this.tickdelta = partialTicks;
		this.matrixStack = matrixStack;
	}
}