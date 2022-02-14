package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;

public class EventBeginRenderTick extends Event {
    private float multiplier = 1;

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }
}
