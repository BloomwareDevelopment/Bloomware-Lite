package me.offeex.bloomware.api.manager.managers;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.api.util.RotationUtil;
import me.offeex.bloomware.mixins.accessors.IPlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static me.offeex.bloomware.Bloomware.mc;

public class RotationManager {
    private float targetYaw, targetPitch;
    private boolean rotate = false;
    private float packetYaw, packetPitch;

    @Subscribe
    public void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && rotate) {
            packetYaw = packet.getYaw(mc.player.getYaw());
            packetPitch = packet.getPitch(mc.player.getPitch());
            ((IPlayerMoveC2SPacket) packet).setYaw(targetYaw);
            ((IPlayerMoveC2SPacket) packet).setPitch(targetPitch);
        }
    }

    public void sendPacket(float yaw, float pitch) {
        packetYaw = yaw;
        packetPitch = pitch;
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(packetYaw, packetPitch, mc.player.isOnGround()));
    }

    public void sendPacket() {
        packetYaw = targetYaw;
        packetPitch = targetPitch;
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(packetYaw, packetPitch, mc.player.isOnGround()));
    }

    public float getPacketYaw() {
        return packetYaw;
    }

    public float getPacketPitch() {
        return packetPitch;
    }

    public float getTargetYaw() {
        return targetYaw;
    }

    public float getTargetPitch() {
        return targetPitch;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setTargetYaw(float targetYaw) {
        this.targetYaw = targetYaw;
        this.targetPitch = mc.player.getPitch();
        rotate = true;
    }

    public void setTargetPitch(float targetPitch) {
        this.targetYaw = mc.player.getYaw();
        this.targetPitch = targetPitch;
        rotate = true;
    }

    public void setRotation(float yaw, float pitch) {
        setTargetYaw(yaw);
        setTargetPitch(pitch);
    }

    public void setRotationBlockPos(BlockPos target) {
        setRotationBlockPos(target, 0, 0);
    }

    public void setRotationBlockPos(BlockPos target, float offsetX, float offsetZ) {
        setRotationVec3d(new Vec3d(target.getX(), target.getY(), target.getZ()), offsetX, offsetZ);
    }

    public void setRotationVec3d(Vec3d target) {
        setRotationVec3d(target, 0, 0);
    }

    public void setRotationVec3d(Vec3d target, float offsetX, float offsetZ) {
        setTargetYaw(RotationUtil.getLookYaw(target, offsetX, offsetZ));
        setTargetPitch(RotationUtil.getLookPitch(target));
    }

    public void reset() {
        rotate = false;
        targetYaw = mc.player.getYaw();
        targetPitch = mc.player.getPitch();
    }
}
