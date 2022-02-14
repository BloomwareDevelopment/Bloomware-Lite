package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.*;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.screen.Freecam;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@Module.Register(name = "LogoutSpot", description = "Renders players logout spots", category = Module.Category.VISUALS)
public class LogoutSpot extends Module {
    private final SettingGroup boxRender = new SettingGroup.Builder("BoxRender").setup(this);
    private final SettingGroup nametag = new SettingGroup.Builder("Nametag").toggleable(true).setup(this);
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").selected("Outline").setup(boxRender);
    private final SettingColor boxColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 0, 0, 255)).setup(boxRender);
    private final SettingColor textColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 0, 0, 255)).setup(nametag);
    private final SettingBool nickname = new SettingBool.Builder("Nickname").value(true).setup(nametag);
    private final SettingBool health = new SettingBool.Builder("Health").value(true).setup(nametag);
    private final SettingBool coordinates = new SettingBool.Builder("Coordinates").value(false).setup(nametag);
    private final SettingBool time = new SettingBool.Builder("Time").value(true).setup(nametag);
    private final SettingNumber lineWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(this);

    private final HashMap<PlayerEntity, PlayerInfo> logouts = new HashMap<>();
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onDisable() {
        logouts.clear();
    }

    private void removeEntity(PlayerEntity entity) {
        for (PlayerEntity e : logouts.keySet()) {
            if (e.getEntityName().equals(entity.getEntityName())) {
                logouts.remove(e);
                break;
            }
        }
    }

    @Subscribe
    private void onJoinWorld(EventJoinWorld event) {
        logouts.clear();
    }

    @Subscribe
    private void onEntityAdded(EventAddedEntity event) {
        if (event.getEntity() instanceof PlayerEntity player && player != mc.player) removeEntity(player);
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        logouts.forEach((pos, player) -> {
            if (mode.is("Outline"))
                RenderUtil.drawOutline(event.matrixStack, new Box(pos.getX() + 0.4, pos.getY(), pos.getZ() + 0.4, pos.getX() - 0.4, pos.getY() + 2, pos.getZ() - 0.4), boxColor.getColor(), lineWidth.getValue());
            else
                RenderUtil.drawFilledBox(event.matrixStack, new Box(pos.getX() + 0.4, pos.getY(), pos.getZ() + 0.4, pos.getX() - 0.4, pos.getY() + 2, pos.getZ() - 0.4), boxColor.getColor());
            if (nametag.isToggled()) drawStrings(player);
        });
    }

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof PlayerListS2CPacket packet && packet.getAction() == PlayerListS2CPacket.Action.REMOVE_PLAYER) {
            for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
                UUID uuid = entry.getProfile().getId();
                PlayerEntity player = mc.world.getPlayerByUuid(uuid);
                if (player != null && player != mc.player)
                    logouts.put(player, new PlayerInfo(player, player.getBlockPos(), getTime()));
            }
        }
    }

    private record PlayerInfo(PlayerEntity player, BlockPos pos, String time) {
    }

    private String getTime() {
        return format.format(Calendar.getInstance().getTime());
    }

    private float calculateWidth(PlayerInfo info) {
        float length = 0;
        if (nickname.getValue()) length += Bloomware.Font.getStringWidth(info.player.getEntityName() + " ");
        if (health.getValue()) length += Bloomware.Font.getStringWidth((int) calculateHealth(info.player) + " HP");
        return length;
    }

    private void drawStrings(PlayerInfo info) {
        double offset = -(calculateWidth(info) / 2);
        double scale = calculateScale(info.player);
        Vec3d pos = new Vec3d(info.pos.getX(), info.pos.getY() + 2, info.pos.getZ() + 1);
        if (nickname.getValue()) {
            final String nickname = info.player.getEntityName() + " ";
            RenderUtil.drawText(nickname, pos, 0, 0.5, scale, offset, textColor.getColor());
            offset += Bloomware.Font.getStringWidth(nickname);
        }
        if (health.getValue()) {
            final float health = calculateHealth(info.player);
            RenderUtil.drawText(String.valueOf((int) health) + " HP", pos, 0, 0.5, scale, offset, textColor.getColor());
            offset += Bloomware.Font.getStringWidth(String.valueOf((int) health) + " HP");
        }
        if (coordinates.getValue()) {
            String text = "XYZ: " + String.join(",", String.valueOf(info.pos.getX()), String.valueOf(info.pos.getY()), String.valueOf(info.pos.getZ()));
            offset = -(Bloomware.Font.getStringWidth(text) / 2);
            RenderUtil.drawText(text, pos, 0, 0.7, scale, offset, textColor.getColor());
        }
        if (time.getValue()) {
            offset = -(Bloomware.Font.getStringWidth(info.time) / 2);
            RenderUtil.drawText(info.time, pos, 0, 0.9, scale, offset, textColor.getColor());
        }
    }

    private double calculateScale(PlayerEntity entity) {
        Freecam freecam = Bloomware.moduleManager.getModule(Freecam.class);
        double distance = freecam.isEnabled() ? freecam.fakeman.distanceTo(entity) : mc.player.distanceTo(entity);
        return distance <= 10 ? 1 : distance * 0.1;
    }

    private float calculateHealth(PlayerEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }
}
