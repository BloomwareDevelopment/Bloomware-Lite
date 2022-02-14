package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.RotationUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

import static me.offeex.bloomware.Bloomware.mc;

@Module.Register(name = "KillAura", description = "Automatically attacks entities", category = Module.Category.PVP)
public class KillAura extends Module {
    private final SettingGroup attack = new SettingGroup.Builder("Attack").setup(this);
    private final SettingNumber range = new SettingNumber.Builder("Range").value(4).min(1).max(6).inc(0.1).setup(attack);
    private final SettingEnum sort = new SettingEnum.Builder("Sort").modes("Distance", "Health").setup(attack);
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Switch", "Sword", "None").setup(attack);
    private final SettingBool delay = new SettingBool.Builder("Delay").value(true).setup(attack);
    private final SettingBool strict = new SettingBool.Builder("Strict").value(true).setup(attack);
    private final SettingGroup rotate = new SettingGroup.Builder("Rotate").toggleable(true).toggled(true).setup(attack);
    private final SettingEnum box = new SettingEnum.Builder("Box").modes("Head", "Chest", "Legs").selected("Head").setup(rotate);
    private final SettingNumber angle = new SettingNumber.Builder("Angle").value(90).min(0).max(180).inc(1).setup(attack);

    private final SettingGroup targets = new SettingGroup.Builder("Targets").setup(this);
    private final SettingBool players = new SettingBool.Builder("Players").value(true).setup(targets);
    private final SettingBool friends = new SettingBool.Builder("Friends").value(false).setup(targets);
    private final SettingBool animals = new SettingBool.Builder("Animals").value(false).setup(targets);
    private final SettingBool hostiles = new SettingBool.Builder("Hostiles").value(true).setup(targets);
    private final SettingBool disableOnDeath = new SettingBool.Builder("DisableOnDeath").value(true).setup(this);

    @Override
    public void onDisable() {
        Bloomware.rotationManager.reset();
    }

    @Override
    public void onTick() {
        if (target() == null) return;

        float fixYaw = RotationUtil.fixYaw(mc.player.getYaw());
        float lookYaw = RotationUtil.getLookYaw(target().getPos());
        float result = Math.abs(RotationUtil.fixYaw(lookYaw - fixYaw));

        if (result > angle.getValue() || mc.player.squaredDistanceTo(target()) >= Math.pow(range.getValue(), 2)) {
            Bloomware.rotationManager.reset();
            return;
        }
        if (rotate.isToggled()) {
            switch (box.getSelectedStr()) {
                case "Head" -> {
                    Bloomware.rotationManager.setRotationVec3d(target().getEyePos());
                    if (shouldRotate()) Bloomware.rotationManager.sendPacket();
                }
                case "Legs" -> {
                    Bloomware.rotationManager.setRotationVec3d(target().getPos());
                    if (shouldRotate()) Bloomware.rotationManager.sendPacket();
                }
            }
        }
        if (!delay.getValue() || mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f) {
            switch (mode.getSelectedStr()) {
                case "Switch" -> {
                    byte slot = InventoryUtil.findItemInHotbar(SwordItem.class);
                    if (slot != -1) {
                        mc.player.getInventory().selectedSlot = slot;
                        attack(target());
                    }
                }
                case "Sword" -> {
                    if (isSword()) attack(target());
                }
                case "None" -> attack(target());
            }
        }
        if (mc.player.isDead() && disableOnDeath.getValue()) disable();
    }

    private Entity target() {
        Entity last = null;
        double min = Double.MAX_VALUE;
        for (Entity e : mc.world.getEntities()) {

//            Check if should attack
            if (!e.isLiving() || !e.isAlive() || !e.isAttackable() || e == mc.player) continue;
            if (e.isPlayer()) {
                if (!players.getValue()) continue;
                else if (Bloomware.friendManager.getType(e.getEntityName()) != null && Bloomware.friendManager.getType(e.getEntityName()) == FriendManager.PersonType.FRIEND && !friends.getValue())
                    continue;
            } else if (e instanceof Monster && !hostiles.getValue()) continue;
            else if ((e instanceof PassiveEntity || e instanceof WaterCreatureEntity || e instanceof AmbientEntity || e instanceof SnowGolemEntity || e instanceof IronGolemEntity) && !animals.getValue())
                continue;

            switch (sort.getSelectedStr()) {
                case "Distance" -> {
                    if (mc.player.squaredDistanceTo(e) < min) {
                        min = mc.player.squaredDistanceTo(e);
                        last = e;
                    }
                }
                case "Health" -> {
                    if (EntityUtil.getFullHealth((LivingEntity) e) <= min) {
                        min = EntityUtil.getFullHealth((LivingEntity) e);
                        last = e;
                    }
                }
            }
        }
        return last;
    }

    private void attack(Entity entity) {
        mc.interactionManager.attackEntity(mc.player, entity);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private boolean isSword() {
        return mc.player.getInventory().getMainHandStack().getItem() instanceof SwordItem;
    }

    public boolean shouldRotate() {
        return mc.player.forwardSpeed == 0 && mc.player.sidewaysSpeed == 0
                && (Bloomware.rotationManager.getPacketYaw() != Bloomware.rotationManager.getTargetYaw()
                || Bloomware.rotationManager.getPacketPitch() != Bloomware.rotationManager.getTargetPitch())
                && mc.player.age % 3 == 0;
    }
}
