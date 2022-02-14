package me.offeex.bloomware.client.module.modules.client;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;

@Module.Register(name = "Font", description = "Allows you to configure client's fonts", category = Module.Category.CLIENT)
public class Font extends Module {
    public final SettingEnum font = new SettingEnum.Builder("Font").modes("JetBrains", "Verdana", "GravityRegular").selected("Verdana").setup(this);
    public final SettingNumber fontSize = new SettingNumber.Builder("Size").value(20).min(10).max(25).inc(1).setup(this);

    private int oldMode = font.getSelected();
    private int oldSize = (int) fontSize.getValue();

    @Override
    public void onDisable() {
        this.enable();
    }

    @Override
    public void onTick() {
        if (oldMode != font.getSelected() || oldSize != (int) fontSize.getValue()) {
            Bloomware.fontUtil.generateFonts("assets/fonts/" + font.getSelectedStr() + ".ttf", (int) fontSize.getValue());
            oldMode = font.getSelected();
            oldSize = (int) fontSize.getValue();
        }
    }
}
