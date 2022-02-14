package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class EventPlayerMove extends Event {
    private MovementType type;
    private Vec3d vec3d;

    public EventPlayerMove(MovementType type, Vec3d vec3d) {
        this.type = type;
        this.vec3d = vec3d;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Vec3d getVec3d() {
        return vec3d;
    }

    public void setVec3d(Vec3d vec3d) {
        this.vec3d = vec3d;
    }

    public void setX(double x) {
        this.vec3d = new Vec3d(x, vec3d.getY(), vec3d.getZ());
    }

    public void setY(double y) {
        this.vec3d = new Vec3d(vec3d.getX(), y, vec3d.getZ());
    }

    public void setZ(double z) {
        this.vec3d = new Vec3d(vec3d.getX(), vec3d.getY(), z);
    }
}
