package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

@Module.Register(name = "Tracers", description = "", category = Module.Category.VISUALS)
public class Tracers extends Module {
    private final SettingNumber range = new SettingNumber.Builder("Range").min(1).max(200).value(150).inc(1).setup(this);
    private final SettingEnum colorMode = new SettingEnum.Builder("Mode").modes("Static", "Distance").selected("Distance").setup(this);

    private final SettingColor colorStatic = new SettingColor.Builder("Color").color(new ColorMutable(255, 255, 255)).setup(this).depend(() -> colorMode.is("Static"), colorMode);

    private final SettingColor colorClose = new SettingColor.Builder("ColorClose").color(new ColorMutable(255, 0, 0)).setup(this).depend(() -> colorMode.is("Distance"), colorMode);
    private final SettingColor colorFar = new SettingColor.Builder("ColorFar").color(new ColorMutable(0, 255, 0)).setup(this).depend(() -> colorMode.is("Distance"), colorMode);
    private final SettingNumber maxDist = new SettingNumber.Builder("MaxDistance").value(60).min(10).max(120).inc(1).setup(this).depend(() -> colorMode.is("Distance"), colorMode);

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        ArrayList<PlayerEntity> players = EntityUtil.selectPlayers(range);
        if (players.isEmpty()) return;
        for (PlayerEntity progress : players) {
            if (progress == mc.player) continue;
            Camera camera = mc.gameRenderer.getCamera();
            Vec3d startPos = new Vec3d(0, 0, 1)
                    .rotateX(-(float) Math.toRadians(camera.getPitch()))
                    .rotateY(-(float) Math.toRadians(camera.getYaw()));
            Vec3d endPos = RenderUtil.smoothMovement(progress).add(0, progress.getStandingEyeHeight(), 0);

            float dist = mc.player.distanceTo(progress);
            ColorMutable c = colorMode.is("Distance") ? fade(colorFar, colorClose, MathHelper.clamp(dist / (float) maxDist.getValue(), 0, 1)) : colorStatic.getColor();
            RenderUtil.drawLine(event.matrixStack, startPos, endPos, c, 4);
        }
    }

    private ColorMutable fade(SettingColor c1, SettingColor c2, float progress) {
        return new ColorMutable(
                (int) (c1.getColor().getRed() * progress + c2.getColor().getRed() * (1 - progress)),
                (int) (c1.getColor().getGreen() * progress + c2.getColor().getGreen() * (1 - progress)),
                (int) (c1.getColor().getBlue() * progress + c2.getColor().getBlue() * (1 - progress)),
                (int) (c1.getColor().getAlpha() * progress + c2.getColor().getAlpha() * (1 - progress)));
    }
}
