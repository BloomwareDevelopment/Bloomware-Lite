package me.offeex.bloomware.api.util;

import me.offeex.bloomware.Bloomware;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import static me.offeex.bloomware.Bloomware.mc;

public final class MovementUtil {

    public static boolean isMoving(PlayerEntity e) {
        return e.forwardSpeed != 0.0f || e.sidewaysSpeed != 0.0f;
    }

    private static double dirSpeed(Direction.Axis axis) {
        return switch (axis) {
            case X -> mc.player.getX() - mc.player.prevX;
            case Z -> mc.player.getZ() - mc.player.prevZ;
            default -> 0.0;
        };
    }

    public static double getPlayerSpeed() {
        float currentTps = Bloomware.sessionManager.getTps() / 1000.0f;
        double dx = Math.abs(mc.player.getX() - mc.player.prevX);
        double dz = Math.abs(mc.player.getZ() - mc.player.prevZ);
//        return ((MathHelper.sqrt((float) (Math.pow(dirSpeed(Direction.Axis.X), 2) + Math.pow(dirSpeed(Direction.Axis.Z), 2))) / currentTps)) * 3.6;
        return Math.sqrt(dx * dx + dz * dz) * 20 * 3.6;
    }

    public static double getVelocity(Entity e) {
        return Math.abs(e.getVelocity().getX()) + Math.abs(e.getVelocity().getY()) + Math.abs(e.getVelocity().getZ());
    }

    public static void center() {
        double centerX = MathHelper.floor(mc.player.getX()) + 0.5;
        double centerZ = MathHelper.floor(mc.player.getZ()) + 0.5;
        if (mc.player.isSprinting()) {
            mc.player.setSprinting(false);
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
        mc.player.updatePosition(centerX, mc.player.getY(), centerZ);
    }
}
