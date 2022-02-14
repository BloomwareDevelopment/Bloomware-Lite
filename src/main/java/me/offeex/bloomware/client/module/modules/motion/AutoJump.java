package me.offeex.bloomware.client.module.modules.motion;

import me.offeex.bloomware.client.module.Module;

@Module.Register(name = "AutoJump", description = "Automatically jumps", category = Module.Category.MOTION)
public class AutoJump extends Module {
    @Override
    public void onDisable() {
        mc.options.keyJump.setPressed(false);
    }

    @Override
    public void onTick() {
        if (mc.player.isOnGround()) {
            mc.options.keyJump.setPressed(true);
        }
    }
}
