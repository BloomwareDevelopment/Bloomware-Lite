package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.api.event.events.EventRemovedEntity;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.api.util.PlayerUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.screen.Freecam;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Module.Register(name = "MobOwner", description = "Renders mob's owner", category = Module.Category.WORLD)
public class MobOwner extends Module {
    private final SettingNumber range = new SettingNumber.Builder("Range").value(50).min(1).max(100).inc(1).setup(this);
    private final SettingColor color = new SettingColor.Builder("Color").color(new ColorMutable(0, 0, 0, 70)).setup(this);

    private final HashMap<LivingEntity, String> entities = new HashMap<>();

    private void selectEntities() {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof LivingEntity livingEntity && mc.player.distanceTo(entity) <= range.getValue() && EntityUtil.getOwnerUUID(livingEntity) != null && !entities.containsKey(livingEntity)) {
                UUID uuid = EntityUtil.getOwnerUUID(livingEntity);
                String owner = PlayerUtil.getNicknameByUUID(uuid);
                entities.put(livingEntity, owner == null ? "unknown" : owner);
            }
        }
    }

    @Subscribe private void onOpenScreen(EventOpenScreen event) {
        if (event.getScreen() instanceof DeathScreen) entities.clear();
    }

    @Subscribe private void onEntityRemoved(EventRemovedEntity event) {
        entities.remove(event.getEntity());
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        selectEntities();
        for (Map.Entry<LivingEntity, String> entity : entities.entrySet()) {
            if (mc.player.distanceTo(entity.getKey()) > range.getValue() || mc.player.getVehicle() == entity.getKey()) continue;
            Vec3d pos = entity.getKey().getPos().subtract(RenderUtil.getInterpolationOffset(entity.getKey())).add(0, entity.getKey().getHeight() + 0.25, 0);
            float stringWidth = Bloomware.Font.getStringWidth("Owned by " + entity.getValue()) + 4;
            double scale = calculateScale(entity.getKey());
            RenderUtil.drawBackground(pos, 0, 0.5, stringWidth, scale, 14, color.getColor());
            RenderUtil.drawText("Owned by " + entity.getValue(), pos, 0, 0.5, scale, -(stringWidth / 2) + 2, ColorMutable.WHITE);
        }
    }

    @Override
    public void onDisable() {
        entities.clear();
    }

    private double calculateScale(LivingEntity entity) {
        Freecam freecam = Bloomware.moduleManager.getModule(Freecam.class);
        double distance = freecam.isEnabled() ? freecam.fakeman.distanceTo(entity) : mc.player.distanceTo(entity);
        return distance <= 10 ? 1 : distance * 0.1;
    }
}
