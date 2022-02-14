package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class EventInteractBlock extends Event {
    private final ClientPlayerEntity clientPlayerEntity;
    private final ClientWorld clientWorld;
    private final Hand hand;
    private final BlockHitResult blockHitResult;
    private ActionResult actionResult;

    public EventInteractBlock(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Hand hand, BlockHitResult blockHitResult) {
        this.clientPlayerEntity = clientPlayerEntity;
        this.clientWorld = clientWorld;
        this.hand = hand;
        this.blockHitResult = blockHitResult;
    }

    public ClientPlayerEntity getClientPlayerEntity() {
        return clientPlayerEntity;
    }

    public ClientWorld getClientWorld() {
        return clientWorld;
    }

    public Hand getHand() {
        return hand;
    }

    public BlockHitResult getBlockHitResult() {
        return blockHitResult;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }
}
