package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;

public class EventBlockBreakingCooldown extends Event {
    private int cooldown;

    public EventBlockBreakingCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
