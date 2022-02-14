package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.util.math.MatrixStack;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Module.Register(name = "Time", description = "Shows current time", category = Module.Category.HUD)
public class Time extends Module {
    private final SettingBool showSeconds = new SettingBool.Builder("ShowSeconds").value(true).setup(this);

    private final SimpleDateFormat patternS = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat patternH = new SimpleDateFormat("HH:mm");

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("Time: " + (showSeconds.getValue() ? patternS.format(Calendar.getInstance().getTime()) : patternH.format(Calendar.getInstance().getTime()))) + 6;
        Bloomware.Font.drawString(stack, "Time: " + (showSeconds.getValue() ? patternS.format(Calendar.getInstance().getTime()) : patternH.format(Calendar.getInstance().getTime())), x + 3, y + 2, ColorUtils.getHudColor());
    }
}
