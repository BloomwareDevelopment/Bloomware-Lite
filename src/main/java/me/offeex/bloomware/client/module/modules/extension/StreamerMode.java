package me.offeex.bloomware.client.module.modules.extension;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.streamermode.StreamerWindowFrame;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.mixins.accessors.IMinecraftClient;
import net.minecraft.util.math.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Module.Register(name = "StreamerMode", description = "Shows private info in different window", category = Module.Category.EXTENSION)
public class StreamerMode extends Module {
    private final SettingGroup hide = new SettingGroup.Builder("Hide").setup(this);
    public final SettingBool hideCoords = new SettingBool.Builder("Coordinates").value(true).setup(hide);
    public final SettingBool hideNickname = new SettingBool.Builder("Nickname").value(true).setup(hide);
    private StreamerWindowFrame window;

    @Override
    public void onEnable() {
        EventQueue.invokeLater(() -> {
            if (window == null) window = new StreamerWindowFrame();
            window.setVisible(true);
        });
    }

    @Override
    public void onDisable() {
        if (window != null) window.setVisible(false);
    }

    @Override
    public void onTick() {
        if (window != null) {
            ArrayList<String> draw = new ArrayList<>(Arrays.asList(
                    Bloomware.NAME + " v" + Bloomware.version,
                    " ",
                    "FPS: " + IMinecraftClient.getCurrentFps(),
                    "Direction: " + (mc.player.getHorizontalFacing().getDirection() == Direction.AxisDirection.POSITIVE ? "+" : "-") + mc.player.getHorizontalFacing().getAxis().asString().toLowerCase(),
                    "XYZ: " + (int) mc.player.getX() + ", " + (int) mc.player.getY() + ", " + (int) mc.player.getZ()
            ));
            window.setStrings(draw);
        }
    }
}
