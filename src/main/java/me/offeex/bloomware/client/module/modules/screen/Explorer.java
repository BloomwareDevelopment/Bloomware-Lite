package me.offeex.bloomware.client.module.modules.screen;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

@Module.Register(name = "Explorer", description = "explorer lol", category = Module.Category.CAMERA)
public class Explorer extends Module {
    private DeathScreen oldScreen;

//    TODO: Make it working!!!

    @Subscribe
    private void onOpenScreen(EventOpenScreen event) {
        if (event.getScreen() instanceof DeathScreen) {
            oldScreen = (DeathScreen) event.getScreen();
            mc.player.setHealth(20);
            mc.setScreenAndRender(null);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player.isDead()) {
            mc.player.setHealth(0f);
            mc.setScreenAndRender(oldScreen);
        }
    }
}
