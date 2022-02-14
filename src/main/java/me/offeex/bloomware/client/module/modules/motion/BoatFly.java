package me.offeex.bloomware.client.module.modules.motion;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

@Module.Register(name = "BoatFly", description = "Allows you fly using boats.", category = Module.Category.MOTION)
public class BoatFly extends Module {
    private final SettingBool packetSend = new SettingBool.Builder("SendPacket").value(false).setup(this);
    private final SettingNumber speed = new SettingNumber.Builder("Speed").value(0.1).min(0).max(5).inc(0.1).setup(this);
    private final SettingBool rotate = new SettingBool.Builder("Rotate").value(true).setup(this);
    private Entity boat = null;

    @Override
    public void onDisable() {
        if (boat != null) {
            boat.setNoGravity(false);
        }
    }

    @Override
    public void onTick() {
        boat = mc.player.getVehicle();
        if (boat != null) {
            Vec3d np = boat.getPos();
            float y = mc.player.getYaw();
            int mx = 0, my = 0, mz = 0;
            if (mc.options.keyJump.isPressed())
                my++;
            if (mc.options.keyBack.isPressed())
                mz++;
            if (mc.options.keyLeft.isPressed())
                mx--;
            if (mc.options.keyRight.isPressed())
                mx++;
            if (mc.options.keyForward.isPressed())
                mz--;
            double ts = 1;
            double s = Math.sin(Math.toRadians(y));
            double c = Math.cos(Math.toRadians(y));
            double nx = ts * mz * s;
            double nz = ts * mz * -c;
            double ny = ts * my;
            nx += ts * mx * -c;
            nz += ts * mx * -s;
            Vec3d nv3 = new Vec3d(nx, ny, nz);
            np = np.add(nv3.multiply(speed.getValue() / 10));
            boat.updatePosition(np.x, np.y, np.z);
            boat.setVelocity(0, 0, 0);
            nv3 = nv3.multiply(0.1);
            boat.addVelocity(nv3.x, nv3.y, nv3.z);

            if (rotate.getValue()) {
                boat.setYaw(mc.player.getYaw());
            }

            if (packetSend.getValue()) {
                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new VehicleMoveC2SPacket(boat));
            }
        }
    }
}
