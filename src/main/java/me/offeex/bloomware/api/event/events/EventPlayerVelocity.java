package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.entity.Entity;

public class EventPlayerVelocity extends Event {
    private final Entity entity;
    private double multX, multY, multZ;

    public EventPlayerVelocity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getMultX() {
        return multX;
    }

    public double getMultY() {
        return multY;
    }

    public double getMultZ() {
        return multZ;
    }

    public void setMultX(double multX) {
        this.multX = multX;
    }

    public void setMultY(double multY) {
        this.multY = multY;
    }

    public void setMultZ(double multZ) {
        this.multZ = multZ;
    }
}
