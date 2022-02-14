package me.offeex.bloomware.client.module.modules.screen;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "CameraClip", description = "Allows you see through walls.", category = Module.Category.CAMERA)
public class CameraClip extends Module {
    public final SettingNumber cameraDistance = new SettingNumber.Builder("CameraDistance").value(4).min(1).max(30).inc(0.1).setup(this);

    /**
     * @see me.offeex.bloomware.mixins.MixinCamera
     */
}
