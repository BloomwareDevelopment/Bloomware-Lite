package me.offeex.bloomware.client.module.modules.client;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.client.gui.screen.TitleScreen;
import org.lwjgl.glfw.GLFW;

@Module.Register(name = "ClickGUI", description = "It's gui", category = Module.Category.CLIENT)
public class Gui extends Module {
    public final SettingColor color = new SettingColor.Builder("Color").setup(this);
    public final SettingGroup hue = new SettingGroup.Builder("Hue").setup(this);
    public final SettingGroup hueDown = new SettingGroup.Builder("Down").toggleable(true).setup(hue);
    public final SettingColor downColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 0, 255, 100)).setup(hueDown);
    public final SettingGroup hueUp = new SettingGroup.Builder("Up").toggleable(true).setup(hue);
    public final SettingColor upColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 255, 0, 100)).setup(hueUp);
    public final SettingEnum categoryOffset = new SettingEnum.Builder("Offset").modes("Center", "Right", "Left").selected("Center").setup(this);
    public final SettingEnum enabledStyle = new SettingEnum.Builder("EnabledStyle").modes("Glow", "None", "Fill").selected("Glow").setup(this);

    public final SettingGroup particles = new SettingGroup.Builder("Particles").toggleable(true).setup(this);

    public final SettingGroup size = new SettingGroup.Builder("Size").setup(particles);
    public final SettingNumber sizeValue = new SettingNumber.Builder("Value").min(1).max(15).inc(1).value(3).setup(size);
    public final SettingNumber sizeDiff = new SettingNumber.Builder("Difference").min(0).max(5).inc(1).value(0).setup(size);

    public final SettingGroup speed = new SettingGroup.Builder("Speed").setup(particles);
    public final SettingNumber speedValue = new SettingNumber.Builder("Value").min(1).max(25).inc(1).value(4).setup(speed);
    public final SettingNumber speedDiff = new SettingNumber.Builder("Difference").min(1).max(10).inc(1).value(1).setup(speed);

    public final SettingGroup wind = new SettingGroup.Builder("Wind").setup(particles);
    public final SettingNumber windValue = new SettingNumber.Builder("Value").min(45).max(135).inc(1).value(90).setup(wind);
    public final SettingNumber windDiff = new SettingNumber.Builder("Difference").min(0).max(25).inc(1).value(0).setup(wind);

    public final SettingGroup colorP = new SettingGroup.Builder("Color").setup(particles);
    public final SettingEnum colorMode = new SettingEnum.Builder("Mode").modes("Static", "Random").selected("Static").setup(colorP);
    public final SettingColor colorParticle = new SettingColor.Builder("Color").color(new ColorMutable(255, 255, 255, 100)).setup(colorP).depend(() -> colorMode.is("Static"), colorMode);
    public final SettingBool glow = new SettingBool.Builder("Glow").setup(colorP);

    public final SettingNumber amount = new SettingNumber.Builder("Amount").min(10).max(500).inc(1).value(150).setup(particles);

    public Gui() {
        this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void toggle() {
        if (!(mc.currentScreen instanceof TitleScreen)) mc.setScreen(Bloomware.gui);
        else Bloomware.currentScreen = Bloomware.gui;
    }

    /**
     * Impl
     * @see me.offeex.bloomware.client.gui.screen.ClickGUI
     */
}
