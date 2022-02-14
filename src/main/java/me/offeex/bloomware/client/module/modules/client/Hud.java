package me.offeex.bloomware.client.module.modules.client;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.screen.TitleScreen;

@Module.Register(name = "HUD", description = "Edit your HUD", category = Module.Category.CLIENT)
public class Hud extends Module {
    @Override
    public void onEnable() {
        if (!(mc.currentScreen instanceof TitleScreen)) mc.setScreen(Bloomware.hud);
        else Bloomware.currentScreen = Bloomware.hud;
        disable();
    }
}
