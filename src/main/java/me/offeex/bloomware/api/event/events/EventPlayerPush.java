package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.entity.Entity;

public class EventPlayerPush extends Event {
    private Entity entity;
    private double x;
    private double z;
    private boolean velocityDirty;

    public EventPlayerPush(Entity entity, double x, double z) {
        this.entity = entity;
        this.x = x;
        this.z = z;
        this.velocityDirty = true;
    }

    public EventPlayerPush() {}

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isVelocityDirty() {
        return velocityDirty;
    }
}
