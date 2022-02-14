package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.screen.Freecam;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.List;

@Module.Register(name = "Nametags", description = "Renders different info about interact above", category = Module.Category.VISUALS)
public class Nametags extends Module {
    private final SettingBool armor = new SettingBool.Builder("Armor").value(true).setup(this);
    private final SettingBool health = new SettingBool.Builder("Health").value(true).setup(this);
    private final SettingBool gamemode = new SettingBool.Builder("Gamemode").value(false).setup(this);
    private final SettingBool self = new SettingBool.Builder("Self").setup(this);
    private final SettingBool ping = new SettingBool.Builder("Ping").value(true).setup(this);
    private final SettingBool healthBar = new SettingBool.Builder("HealthBar").value(false).setup(this);
    private final SettingBool distance = new SettingBool.Builder("Distance").value(false).setup(this);
    private final SettingNumber range = new SettingNumber.Builder("Range").min(1).max(200).value(150).inc(1).setup(this);
    private final SettingColor color = new SettingColor.Builder("Color").color(new ColorMutable(0, 0, 0, 70)).setup(this);

    private final ColorMutable ENEMY = new ColorMutable(255, 0, 0, 255);
    private final ColorMutable FRIEND = new ColorMutable(5, 235, 252, 255);
    private final ColorMutable GRAY = new ColorMutable(150, 150, 150, 255);

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        List<PlayerEntity> players = EntityUtil.selectPlayers(range);
        if (players.isEmpty()) return;
        for (PlayerEntity p : players) {
            if (p == mc.player && !self.getValue()) continue;
            Vec3d pos = RenderUtil.smoothMovement(p).add(0, p.getHeight() + 0.25, 0);
            RenderUtil.drawBackground(pos, 0, 0.5, getWidth(p), getScale(p), 14, color.getColor());
            if (healthBar.getValue())
                RenderUtil.drawBackground(pos, 0, 0.5, getWidth(getWidth(p), p), getScale(p), 1, getHealthColor(p));
            drawStrings(p, pos);
            if (armor.getValue()) drawItems(p, pos);
        }
    }

    private void drawStrings(PlayerEntity entity, Vec3d pos) {
        double offset = -(getWidth(entity) / 2) + 2;
        double scale = getScale(entity);

//        Ping visuals
        if (ping.getValue()) {
            RenderUtil.drawText(EntityUtil.getEntityPing(entity) + "ms", pos, 0, 0.5, scale, offset, GRAY);
            offset += Bloomware.Font.getStringWidth(EntityUtil.getEntityPing(entity) + "ms") + 2;
        }

//        Gamemode visuals
        if (gamemode.getValue()) {
            RenderUtil.drawText(translateGamemode(EntityUtil.getEntityGamemode(entity)), pos, 0, 0.5, scale, offset, ColorMutable.WHITE);
            offset += Bloomware.Font.getStringWidth(translateGamemode(EntityUtil.getEntityGamemode(entity))) + 2;
        }

//        Name visuals
        RenderUtil.drawText(entity.getEntityName(), pos, 0, 0.5, scale, offset, getNicknameColor(entity));
        offset += Bloomware.Font.getStringWidth(entity.getEntityName()) + 2;

//        Health visuals
        if (health.getValue()) {
            final String health = String.valueOf((int) EntityUtil.getFullHealth(entity));
            RenderUtil.drawText(health, pos, 0, 0.5, scale, offset, getHealthColor(entity));
            if (distance.getValue()) offset += Bloomware.Font.getStringWidth(health) + 2;
        }

        if (distance.getValue()) {
            final String distance = String.format("%.1f", mc.player.distanceTo(entity)) + "m";
            RenderUtil.drawText(distance, pos, 0, 0.5, scale, offset, GRAY);
        }
    }

    private void drawItems(PlayerEntity entity, Vec3d pos) {
        List<ItemStack> items = new ArrayList<>();
        double scale = 0.5 * getScale(entity);

        if (!entity.getMainHandStack().isEmpty()) items.add(entity.getMainHandStack());
        for (ItemStack stack : entity.getArmorItems()) if (!stack.isEmpty()) items.add(stack);
        if (!entity.getOffHandStack().isEmpty()) items.add(entity.getOffHandStack());

        double step = scale; // Width of each item
        double space = scale * 0; // Space between items

        for (int i = 0; i < items.size(); i++)
            RenderUtil.drawItem(items.get(i), pos, scale, -((step * items.size() + space * (items.size() - 1)) / 2d) + (step + space) * (i + 0.5), 0.5 + scale * 0.5);
    }

    private float getWidth(PlayerEntity entity) {
        float length = Bloomware.Font.getStringWidth(entity.getEntityName()) + 2;
        if (ping.getValue()) length += Bloomware.Font.getStringWidth(EntityUtil.getEntityPing(entity) + "ms") + 2;
        if (gamemode.getValue())
            length += Bloomware.Font.getStringWidth(translateGamemode(EntityUtil.getEntityGamemode(entity))) + 2;
        if (health.getValue())
            length += Bloomware.Font.getStringWidth(String.valueOf((int) EntityUtil.getFullHealth(entity))) + 2;
        if (distance.getValue()) length += Bloomware.Font.getStringWidth(String.format("%.1f", mc.player.distanceTo(entity)) + "m") + 2;
        return length + 2;
    }

    public double getScale(PlayerEntity entity) {
        Freecam freecam = Bloomware.moduleManager.getModule(Freecam.class);
        double distance = freecam.isEnabled() ? freecam.fakeman.distanceTo(entity) : mc.player.distanceTo(entity);
        return distance <= 10 ? 1 : distance * 0.1;
    }

    private ColorMutable getNicknameColor(PlayerEntity entity) {
        if (Bloomware.friendManager.getType(entity.getEntityName()) == null) return ColorMutable.WHITE;
        else if (Bloomware.friendManager.getType(entity.getEntityName()) == FriendManager.PersonType.FRIEND)
            return FRIEND;
        return ENEMY;
    }

    private float getWidth(float len, PlayerEntity entity) {
        float health = EntityUtil.getFullHealth(entity);
        if (health >= 20) return len;
        return (float) len / 20 * health;
    }

    private String translateGamemode(GameMode gamemode) {
        if (gamemode == null) return "[BOT]";
        return switch (gamemode) {
            case SURVIVAL -> "[S]";
            case CREATIVE -> "[C]";
            case SPECTATOR -> "[SP]";
            case ADVENTURE -> "[A]";
        };
    }

    private ColorMutable getHealthColor(PlayerEntity entity) {
        int health = (int) EntityUtil.getFullHealth(entity);
        if (health <= 15 && health > 7) return ColorMutable.YELLOW;
        if (health > 15) return ColorMutable.GREEN;
        return ColorMutable.RED;
    }
}
