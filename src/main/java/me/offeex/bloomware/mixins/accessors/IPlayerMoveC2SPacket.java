package me.offeex.bloomware.mixins.accessors;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerMoveC2SPacket.class)
public interface IPlayerMoveC2SPacket {
    @Mutable @Accessor("x")
    void setX(double x);

    @Mutable @Accessor("y")
    void setY(double y);

    @Mutable @Accessor("z")
    void setZ(double z);

    @Mutable @Accessor("yaw")
    void setYaw(float yaw);

    @Mutable @Accessor("pitch")
    void setPitch(float pitch);

    @Mutable @Accessor("onGround")
    void setOnGround(boolean state);
}
