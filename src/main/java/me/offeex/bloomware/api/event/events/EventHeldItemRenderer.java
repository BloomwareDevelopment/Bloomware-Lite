package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class EventHeldItemRenderer extends Event {
    private final Hand hand;
    private final ItemStack item;
    private float ep;
    private final MatrixStack stack;

    public EventHeldItemRenderer(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light) {
        this.hand = hand;
        this.item = item;
        this.ep = equipProgress;
        this.stack = stack;
    }

//    public static class Cancelled extends EventHeldItemRenderer {
//
//        public Cancelled(Hand hand, MatrixStack matrices) {
//            this.hand = hand;
//            this.stack = matrices;
//        }
//  }

    public Hand getHand() {
        return hand;
    }

    public ItemStack getItem() {
        return item;
    }

    public float getEp() {
        return ep;
    }

    public MatrixStack getStack() {
        return stack;
    }
}
