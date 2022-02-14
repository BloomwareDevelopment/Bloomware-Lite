package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import me.offeex.bloomware.mixins.accessors.IMinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Module.Register(name = "InventoryMove", description = "Allows you to move when GUI is open", category = Module.Category.MOTION)
public class InventoryMove extends Module {
    private final SettingNumber speedModifier = new SettingNumber.Builder("RotationSpeed").value(5).min(1).max(50).inc(1).setup(this);

    @Subscribe
    private void onRender(EventWorldRender event) {
        if (mc.currentScreen == null || mc.currentScreen instanceof ChatScreen) return;
        float speed = (float) (speedModifier.getValue() / IMinecraftClient.getCurrentFps() * 10);
        long h = mc.getWindow().getHandle();
        mc.player.setYaw(mc.player.getYaw() + (InputUtil.isKeyPressed(h, GLFW.GLFW_KEY_RIGHT) ? speed : (InputUtil.isKeyPressed(h, GLFW.GLFW_KEY_LEFT) ? -speed : 0)));
        mc.player.setPitch(mc.player.getPitch() + (InputUtil.isKeyPressed(h, GLFW.GLFW_KEY_UP) ?
                mc.player.getPitch() - speed >= -90 ? -speed : 0 :
                (InputUtil.isKeyPressed(h, GLFW.GLFW_KEY_DOWN) ? mc.player.getPitch() + speed <= 90 ? speed : 0 : 0)));
    }
}
