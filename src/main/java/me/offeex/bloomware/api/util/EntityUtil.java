package me.offeex.bloomware.api.util;

import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;
import static me.offeex.bloomware.Bloomware.mc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityUtil {
    private static final ArrayList<PlayerEntity> list = new ArrayList<>();

    public static int getEntityPing(PlayerEntity entity) {
        if (mc.getNetworkHandler() == null) return 0;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(entity.getUuid());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }

    public static GameMode getEntityGamemode(PlayerEntity entity) {
        if (entity == null) return null;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(entity.getUuid());
        return playerListEntry == null ? null : playerListEntry.getGameMode();
    }

    public static UUID getOwnerUUID(LivingEntity livingEntity) {
        if (livingEntity instanceof TameableEntity tameableEntity) {
            if (tameableEntity.isTamed()) {
                return tameableEntity.getOwnerUuid();
            }
        }
        if (livingEntity instanceof HorseBaseEntity horseBaseEntity) {
            return horseBaseEntity.getOwnerUuid();
        }
        return null;
    }

    public static float getFullHealth(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static void selectEntities(List<Entity> list, SettingNumber range) {
        list.clear();
        for (Entity entity : mc.world.getEntities())
            if (mc.player.distanceTo(entity) <= range.getValue()) list.add(entity);
    }

    public static ArrayList<PlayerEntity> selectPlayers(SettingNumber range) {
        list.clear();
        for (Entity entity : mc.world.getEntities()) {
            if (mc.player.distanceTo(entity) <= range.getValue() && entity instanceof PlayerEntity playerEntity)
                list.add(playerEntity);
        }
        return list;
    }
}
