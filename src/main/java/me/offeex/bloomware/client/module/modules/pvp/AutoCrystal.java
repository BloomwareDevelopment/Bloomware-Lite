package me.offeex.bloomware.client.module.modules.pvp;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

/*
    Test crystal aura module
*/

@Module.Register(name = "AutoCrystal", description = "Destroys your enemies with end crystals", category = Module.Category.PVP)
public class AutoCrystal extends Module {
    private final SettingBool doPlace = new SettingBool.Builder("Place").value(true).setup(this);
    private final SettingBool doBreak = new SettingBool.Builder("Break").value(true).setup(this);
    private final SettingNumber range = new SettingNumber.Builder("Range").min(1).max(5).inc(0.1).value(5).setup(this);
    private final SettingEnum placeMode = new SettingEnum.Builder("PlaceMode").modes("1.12", "1.13+").selected("1.13+").setup(this);
    private final SettingBool autoSwitch = new SettingBool.Builder("AutoSwitch").value(true).setup(this);
    private final SettingNumber breakTick = new SettingNumber.Builder("BreakDelay").min(1).max(20).inc(1).value(10).setup(this);
    private final SettingNumber placeTick = new SettingNumber.Builder("PlaceDelay").min(1).max(20).inc(1).value(10).setup(this);
    private final SettingGroup render = new SettingGroup.Builder("Render").setup(this);

    private final SettingGroup breaking = new SettingGroup.Builder("Breaking").toggleable(true).setup(render);
    private final SettingEnum breakingMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").selected("Fill").setup(breaking);
    private final SettingColor breakingColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 0, 0, 255)).setup(breaking);
    private final SettingNumber breakWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(breaking);

    private final SettingGroup placing = new SettingGroup.Builder("Placing").toggleable(true).setup(render);
    private final SettingEnum placingMode = new SettingEnum.Builder("PlacingMode").modes("Fill", "Outline").selected("Fill").setup(placing);
    private final SettingColor placingColor = new SettingColor.Builder("PlacingColor").color(new ColorMutable(0, 255, 0, 255)).setup(placing);
    private final SettingNumber placeWidth = new SettingNumber.Builder("PlaceWidth").min(1).max(5).inc(0.1).value(4).setup(placing);

    private final SettingGroup antiSuicide = new SettingGroup.Builder("AntiSuicide").toggleable(true).toggled(true).setup(this);
    private final SettingNumber stopHp = new SettingNumber.Builder("StopHP").min(1).max(20).inc(1).value(10).setup(antiSuicide);

    private final SettingGroup target = new SettingGroup.Builder("Targets").setup(this);
    private final SettingBool players = new SettingBool.Builder("Players").value(true).setup(target);
    private final SettingBool friends = new SettingBool.Builder("Friends").value(false).setup(target);
    private final SettingBool animals = new SettingBool.Builder("Animals").value(false).setup(target);
    private final SettingBool hostiles = new SettingBool.Builder("Hostiles").value(true).setup(target);

    private final SettingEnum sortMode = new SettingEnum.Builder("SortMode").modes("Health", "Distance").selected("Health").setup(this);
    private final SettingBool rotate = new SettingBool.Builder("Rotate").value(true).setup(this);

    private final SettingBool offhand = new SettingBool.Builder("OffHand").value(false).setup(this);

    private final ArrayList<EndCrystalEntity> crystals = new ArrayList<>();

    @Override
    public void onDisable() {
        Bloomware.rotationManager.reset();
        crystals.clear();
    }

    @Override
    public void onTick() {
        if (antiSuicide.isToggled() && mc.player.getHealth() <= stopHp.getValue()) return;
        if (offhand.getValue()) {
            if (mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL) {
                for (int slot = 9; slot < 45; slot++) {
                    if (mc.player.getInventory().getStack(slot >= 36 ? slot - 36 : slot).getItem() == Items.END_CRYSTAL) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                        if (!mc.player.getOffHandStack().isEmpty()) {
                            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
                        }
                    }
                }
            }
        }
        LivingEntity target = (LivingEntity) target();
        if (target == null) return;
        if (doPlace.getValue()) {
            for (BlockPos pos : getCrystalPositions(target)) {
                if (autoSwitch.getValue()) switchToCrystal();
                if ((mc.player.getInventory().getMainHandStack().getItem() == Items.END_CRYSTAL || mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL) && mc.player.age % placeTick.getValue() == 0) {
                    if (rotate.getValue()) Bloomware.rotationManager.setRotationBlockPos(pos);
//                    TODO: Fix the prikol
                    mc.interactionManager.interactBlock(mc.player, mc.world, offhand.getValue() ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos), Direction.DOWN, pos, false));
//                    mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(offhand.getValue() ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos), Direction.DOWN, pos, false)));
                    mc.player.swingHand(offhand.getValue() ? Hand.OFF_HAND : Hand.MAIN_HAND);
                    break;
                }
            }
        }
        findCrystals();
        for (EndCrystalEntity crystal : crystals) {
            if (doBreak.getValue() && mc.player.age % breakTick.getValue() == 0) {
                if (rotate.getValue()) Bloomware.rotationManager.setRotationBlockPos(crystal.getBlockPos());
                mc.interactionManager.attackEntity(mc.player, crystal);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        LivingEntity target = (LivingEntity) target();
        if (target == null) return;
        for (BlockPos pos : getCrystalPositions(target)) {
            if (placing.isToggled()) {
                if (placingMode.is("Fill"))
                    RenderUtil.drawFilledBox(event.matrixStack, new Box(pos), placingColor.getColor());
                else
                    RenderUtil.drawOutline(event.matrixStack, new Box(pos), placingColor.getColor(), placeWidth.getValue());
            }
        }
        for (EndCrystalEntity crystal : crystals) {
            if (breaking.isToggled()) {
                if (breakingMode.is("Fill"))
                    RenderUtil.drawFilledBox(event.matrixStack, new Box(crystal.getBlockPos()), breakingColor.getColor());
                else
                    RenderUtil.drawOutline(event.matrixStack, new Box(crystal.getBlockPos()), breakingColor.getColor(), breakWidth.getValue());
            }
        }
    }

    private ArrayList<BlockPos> getCrystalPositions(LivingEntity target) {
        final ArrayList<BlockPos> posses = new ArrayList<>();
        for (int x = (int) (target.getX() - 1); x <= target.getX() + 1; x++) {
            for (int y = (int) (target.getY() - 1); y <= target.getY() + 1; y++) {
                for (int z = (int) (target.getZ() - 1); z <= target.getZ() + 1; z++) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (canPlaceCrystal(pos)) posses.add(pos);
                }
            }
        }
        return posses;

    }

    private void findCrystals() {
        crystals.clear();
        mc.world.getEntities().forEach(entity -> {
            if (entity instanceof EndCrystalEntity crystal && mc.player.distanceTo(entity) <= range.getValue())
                crystals.add(crystal);
        });
    }

    private Entity target() {
        Entity last = null;
        double min = Double.MAX_VALUE;
        for (Entity e : mc.world.getEntities()) {
            if (!e.isLiving() || !e.isAlive() || !e.isAttackable() || e == mc.player || mc.player.distanceTo(e) > range.getValue()) continue;
            if (e.isPlayer()) {
                if (!players.getValue()) continue;
                else if (Bloomware.friendManager.getType(e.getEntityName()) != null && Bloomware.friendManager.getType(e.getEntityName()) == FriendManager.PersonType.FRIEND && !friends.getValue())
                    continue;
            } else if (e instanceof Monster && !hostiles.getValue()) continue;
            else if ((e instanceof PassiveEntity || e instanceof WaterCreatureEntity || e instanceof AmbientEntity || e instanceof SnowGolemEntity || e instanceof IronGolemEntity) && !animals.getValue())
                continue;

            switch (sortMode.getSelectedStr()) {
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

    private boolean canPlaceCrystal(BlockPos pos) {
        return (mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) && mc.world.isAir(pos.up());
    }

    private void switchToCrystal() {
        byte slot = InventoryUtil.findItemInHotbar(Items.END_CRYSTAL);
        if (slot != -1) mc.player.getInventory().selectedSlot = slot;
    }
}
