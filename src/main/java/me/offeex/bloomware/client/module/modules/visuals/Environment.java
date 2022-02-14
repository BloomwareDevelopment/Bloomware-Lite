package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Module.Register(name = "Environment", description = "Changes various color of the utilities", category = Module.Category.VISUALS)
public class Environment extends Module {
    public final SettingGroup sky = new SettingGroup.Builder("Sky").toggleable(true).setup(this);
    public final SettingColor skyColor = new SettingColor.Builder("Color").color(new ColorMutable(100, 100, 100, 255)).setup(sky);
    public final SettingGroup clouds = new SettingGroup.Builder("Clouds").toggleable(true).setup(this);
    public final SettingColor cloudColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 255, 255, 255)).setup(clouds);
    public final SettingGroup grass = new SettingGroup.Builder("Grass").toggleable(true).setup(this);
    public final SettingColor grassColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 206, 68, 255)).setup(grass);
    public final SettingGroup water = new SettingGroup.Builder("Water").toggleable(true).setup(this);
    public final SettingColor waterColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 158, 206, 255)).setup(water);
    public final SettingGroup leaves = new SettingGroup.Builder("Leaves").toggleable(true).setup(this);
    public final SettingColor leavesColor = new SettingColor.Builder("Color").color(new ColorMutable(27, 252, 42, 255)).setup(leaves);
    private final SettingGroup timeChange = new SettingGroup.Builder("TimeChange").toggleable(true).setup(this);
    private final SettingNumber time = new SettingNumber.Builder("Time").value(18000).min(0).max(24000).inc(1).setup(timeChange);
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Static", "Sync").selected("Static").setup(timeChange);

    private long oldTime = 0;

    @Override
    public void onEnable() {
        mc.worldRenderer.reload();
        if (timeChange.isToggled()) oldTime = mc.world.getTime();
    }

    @Override
    public void onDisable() {
        mc.worldRenderer.reload();
        if (timeChange.isToggled()) mc.world.setTimeOfDay(oldTime);
    }

    @Override
    public void onTick() {
        if (!timeChange.isToggled()) return;
        mc.world.setTimeOfDay(mode.is("static") ? (long) time.getValue() : convertToTicks(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())) - 3000);
    }

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (!timeChange.isToggled()) return;
        event.setCancelled(event.getPacket() instanceof WorldTimeUpdateS2CPacket);
    }

    private long convertToTicks(String time) {
        String[] data = time.split(":");
        return (Integer.parseInt(data[0]) * 1000L) + (Integer.parseInt(data[1]) * 16L);
    }
}
