package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.entity.Entity;
import static me.offeex.bloomware.Bloomware.mc;

public class EventRemovedEntity extends Event {
    private final int id;
    private final Entity.RemovalReason reason;

    public EventRemovedEntity(int id, Entity.RemovalReason reason) {
        this.id = id;
        this.reason = reason;
    }

    public Entity getEntity() {
        return mc.world.getEntityById(id);
    }

    public Entity.RemovalReason getReason() {
        return this.reason;
    }
}
