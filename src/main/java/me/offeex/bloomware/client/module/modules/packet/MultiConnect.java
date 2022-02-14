package me.offeex.bloomware.client.module.modules.packet;

import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.screen.TitleScreen;

@Module.Register(name = "MultiConnect", description = "Allows you to connect several servers in one window.", category = Module.Category.PACKET)
public class MultiConnect extends Module {
//    TODO: Rewrite

    @Override
    public void onEnable() {
        mc.disconnect(new TitleScreen());
        this.disable();
    }
}
