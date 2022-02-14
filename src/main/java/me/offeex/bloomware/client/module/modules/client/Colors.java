package me.offeex.bloomware.client.module.modules.client;

import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;

@Module.Register(name = "Colors", description = "Configures colors", category = Module.Category.CLIENT)
public class Colors extends Module {
    public final SettingColor textColor = new SettingColor.Builder("Text").color(new ColorMutable(20, 255, 45)).setup(this);
    public final SettingColor sliderColor = new SettingColor.Builder("Slider").color(new ColorMutable(20, 100, 255)).setup(this);
    public final SettingColor hudColor = new SettingColor.Builder("Hud").color(new ColorMutable(100, 100, 100)).setup(this);
    public final SettingEnum blendMode = new SettingEnum.Builder("BlendMode").modes("Normal", "Add").selected("Add").setup(this);
    public final SettingEnum widthMode = new SettingEnum.Builder("WidthMode").modes("2D", "3D").setup(this);

    public final SettingColor elementFrame = new SettingColor.Builder("ElementFrame").color(new ColorMutable("#1c1c1c")).setup(this);

    @Override
    public void onEnable() {
        this.disable();
    }
}
