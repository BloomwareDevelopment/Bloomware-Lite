package me.offeex.bloomware.client.module.modules.client;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.screen.TitleScreen;

@Module.Register(name = "Accounts", description = "Manage your alts & proxies", category = Module.Category.CLIENT)
public class Accounts extends Module {
    @Override
    public void onEnable() {
        if (!(mc.currentScreen instanceof TitleScreen)) mc.setScreen(Bloomware.acc);
        else Bloomware.currentScreen = Bloomware.acc;
        disable();
    }
}
