package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.entity.Entity;

public class EventAddedEntity extends Event {
    private final int id;
    private final Entity entity;

    public EventAddedEntity(int id, Entity entity) {
        this.entity = entity;
        this.id = id;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public int getId() {
        return id;
    }
}
