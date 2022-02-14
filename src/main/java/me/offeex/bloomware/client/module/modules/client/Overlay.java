package me.offeex.bloomware.client.module.modules.client;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;

@Module.Register(name = "Overlay", description = "Enables overlay", category = Module.Category.CLIENT)
public class Overlay extends Module {
    @Override
    public void onEnable() {
        mc.setScreen(Bloomware.overlay);
        disable();
    }
}
