package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.api.event.events.EventSlowDown;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

@Module.Register(name = "Speed", description = "Allows you to go faster.", category = Module.Category.MOTION)
public class Speed extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Legit", "Strafe").selected("Legit").setup(this);
    private final SettingNumber speed = new SettingNumber.Builder("Speed").min(0.1).max(2.0).inc(0.01).value(0.27).setup(this);
    private final SettingBool autoSprint = new SettingBool.Builder("AutoSprint").setup(this);

    @Subscribe
    private void onShouldSlowDown(EventSlowDown e) {
//        TODO: Don't stop moving when on edge of block
        e.setCancelled(mode.is("Strafe"));
    }

    @Subscribe
    private void onKeyPress(EventKeyPress event) {
        if (mode.is("Legit")) {
            if (event.getKey() == GLFW.GLFW_KEY_W || event.getKey() == GLFW.GLFW_KEY_S || event.getKey() == GLFW.GLFW_KEY_A || event.getKey() == GLFW.GLFW_KEY_D) {
                mc.options.keyJump.setPressed(true);
                mc.player.setSprinting(true);
            } else {
                mc.options.keyJump.setPressed(false);
            }
        }
    }

    @Override
    public void onTick() {
        if (mode.is("Strafe")) {
            if (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) {
                if (!mc.player.isSprinting()) mc.player.setSprinting(autoSprint.getValue());

                mc.player.setVelocity(new Vec3d(0, mc.player.getVelocity().getY(), 0));
                mc.player.updateVelocity((float) speed.getValue(), new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));

                double velocityXZ = Math.abs(mc.player.getVelocity().getX()) + Math.abs(mc.player.getVelocity().getZ());
                if (mc.player.isOnGround() && velocityXZ > 0.12) mc.player.jump();
            } else mc.player.setVelocity(new Vec3d(0, mc.player.getVelocity().getY(), 0));
        }
    }
}
