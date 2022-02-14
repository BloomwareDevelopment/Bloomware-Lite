package me.offeex.bloomware.api.util;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static me.offeex.bloomware.Bloomware.mc;

public class RotationUtil {
    public static final int[] yawAngles = {0, 45, 90, 135, 180, -180, -135, -90, -45, 0};
    public static final int[] pitchAngles = {0, 15, 30, 45, 60, 75, 90, -90, -75, -60, -45, -30, -15, 0};

    public static float fixYaw(float yaw) {
        return MathHelper.wrapDegrees(yaw);
    }

    public static float roundYaw() {
        float yaw = fixYaw(mc.player.getYaw());
        for (int i = 0; i < yawAngles.length - 1; i++) {
            if (yaw > yawAngles[i] && yaw < yawAngles[i + 1])
                return Math.abs(yaw - yawAngles[i]) < Math.abs(yaw - yawAngles[i + 1]) ? yawAngles[i] : yawAngles[i + 1];
        }
        return yaw;
    }

    public static float roundPitch() {
        float pitch = mc.player.getPitch();
        for (int i = 0; i < pitchAngles.length - 1; i++) {
            if (pitch > pitchAngles[i] && pitch < pitchAngles[i + 1])
                return Math.abs(pitch - pitchAngles[i]) < Math.abs(pitch - pitchAngles[i + 1]) ? pitchAngles[i] : pitchAngles[i + 1];
        }
        return pitch;
    }

    public static float getLookYaw(Vec3d target) {
        return getLookYaw(target, 0, 0);
    }

    public static float getLookYaw(Vec3d target, float offsetX, float offsetZ) {
        double dx = target.getX() + offsetX - mc.player.getEyePos().x;
        double dz = target.getZ() + offsetZ - mc.player.getEyePos().z;
        return MathHelper.wrapDegrees((float)(MathHelper.atan2(dz, dx) * 57.2957763671875) - 90.0f);
    }

    public static float getLookPitch(Vec3d target) {
        double dx = target.getX() + 0.5 - mc.player.getEyePos().x;
        double dy = target.getY() - mc.player.getEyePos().y;
        double dz = target.getZ() + 0.5 - mc.player.getEyePos().z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        return MathHelper.wrapDegrees((float)(-(MathHelper.atan2(dy, distance) * 57.2957763671875)));
    }
}
