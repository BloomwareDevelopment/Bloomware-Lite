package me.offeex.bloomware.client.module.modules.screen;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.api.util.RotationUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import org.lwjgl.glfw.GLFW;
import java.util.Arrays;

@Module.Register(name = "RotationLock", description = "Blocks rotations of your head.", category = Module.Category.CAMERA)
public class RotationLock extends Module {
    private final SettingBool keys = new SettingBool.Builder("Keys").value(true).setup(this);
    private final SettingBool yawLock = new SettingBool.Builder("YawLock").setup(this);
    private final SettingBool pitchLock = new SettingBool.Builder("PitchLock").setup(this);

    @Override
    public void onTick() {
        if (yawLock.getValue()) Arrays.stream(RotationUtil.yawAngles).filter(value -> RotationUtil.fixYaw(mc.player.getYaw()) != value).findFirst().ifPresent(value -> {
            mc.player.setYaw(RotationUtil.roundYaw());
        });
        if (pitchLock.getValue()) Arrays.stream(RotationUtil.pitchAngles).filter(value -> mc.player.getPitch() != value).findFirst().ifPresent(value -> {
            mc.player.setPitch(RotationUtil.roundPitch());
        });
    }

    @Subscribe
    private void onKeyPress(EventKeyPress event) {
        if (!keys.getValue()) return;
        switch (event.getKey()) {
            case GLFW.GLFW_KEY_UP -> mc.player.setPitch((float) (mc.player.getPitch() - 15));
            case GLFW.GLFW_KEY_DOWN -> mc.player.setPitch((float) (mc.player.getPitch() + 15));
            case GLFW.GLFW_KEY_RIGHT -> mc.player.setYaw((float) (mc.player.getYaw() + 45));
            case GLFW.GLFW_KEY_LEFT -> mc.player.setYaw((float) (mc.player.getYaw() - 45));
        }
    }
}
