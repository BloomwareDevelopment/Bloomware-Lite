package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.MovementUtil;
import me.offeex.bloomware.api.util.WorldUtil;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module.Register(name = "Surround", description = "Surrounds you with obsidian", category = Module.Category.PVP)
public class Surround extends Module {
    private final SettingNumber bpt = new SettingNumber.Builder("BlocksPerTick").min(1).max(20).value(4).setup(this);
    private final SettingBool autoCenter = new SettingBool.Builder("AutoCenter").value(true).setup(this);
    private final SettingBool autoDisable = new SettingBool.Builder("AutoDisable").setup(this);
    private final SettingBool head = new SettingBool.Builder("AboveHead").setup(this);
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Strict", "AirPlace").setup(this);
    private final SettingBool rotate = new SettingBool.Builder("Rotate").setup(this);
    private final SettingNumber fallSpeed = new SettingNumber.Builder("FallSpeed").min(0).max(5).inc(0.1).setup(this);

    private int oldSlot = -1;
    private int placedBlocks = 0;
    private boolean placed;

    @Override
    public void onEnable() {
        oldSlot = mc.player.getInventory().selectedSlot;
        placedBlocks = 0;
        if (hasObsidian()) {
            if (!mc.player.isOnGround()) mc.player.addVelocity(mc.player.getVelocity().x, -fallSpeed.getValue(), mc.player.getVelocity().z);
        } else CommandManager.addChatMessage(Formatting.RED + "No blocks found. Surround has been disabled");
    }

    @Override
    public void onDisable() {
        if (Bloomware.rotationManager.isRotate() && rotate.getValue()) Bloomware.rotationManager.reset();
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));
        mc.player.getInventory().selectedSlot = oldSlot;
    }

    @Override
    public void onTick() {
//        TODO: Fix AboveHead ticks placing
        if (hasObsidian()) {
            if (mc.player.horizontalSpeed != 0) {
                mc.options.keySprint.setPressed(false);
                mc.options.keyForward.setPressed(false);
                mc.options.keyRight.setPressed(false);
                mc.options.keyLeft.setPressed(false);
                mc.options.keyBack.setPressed(false);
                mc.player.setVelocity(0, mc.player.getVelocity().getY(), 0);
                if (autoCenter.getValue()) MovementUtil.center();
            }
            if (mc.player.isOnGround()) {
                placed = true;
                placeBlocks();
            }
        }
        if ((autoDisable.getValue() && placedBlocks == 0 && placed) || !hasObsidian()) disable();
        placedBlocks = 0;
        placed = false;
    }

    private void placeBlocks() {
        ArrayList<BlockPos> positions = new ArrayList<>();
        if (mode.is("Strict")) positions.addAll(getPositions(-1));
        positions.addAll(getPositions(0));

        if (head.getValue()) {
            Direction dir = WorldUtil.getBlockSide(mc.player.getBlockPos().up(2));
            if (mode.is("Strict") && dir == null) {
                positions.add(mc.player.getBlockPos().north().up());
                positions.add(mc.player.getBlockPos().north().up(2));
            }
            positions.add(mc.player.getBlockPos().up(2));
        }

        if (positions.isEmpty() && rotate.getValue() && Bloomware.rotationManager.isRotate()) {
            Bloomware.rotationManager.reset();
            return;
        }

        for (BlockPos pos : positions) {
            if (!(WorldUtil.isPlaceable(pos, true) && placedBlocks < bpt.getValue())) continue;

            byte slot = InventoryUtil.findItemInHotbar(Items.OBSIDIAN);
            Hand hand = InventoryUtil.findHandItem(Items.OBSIDIAN);
            if (slot != -1) mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            Direction direction = WorldUtil.getBlockSide(pos);
//            Checks if there are no blocks around block we're standing on
            if (direction == null && !mode.is("AirPlace")) continue;
            if (rotate.getValue()) {
                Bloomware.rotationManager.setRotationBlockPos(pos, 0.5f, 0.5f);
                Bloomware.rotationManager.sendPacket();
            }
//            TODO: Как ждать?
            boolean head = pos.equals(mc.player.getBlockPos().up(2));
            BlockHitResult result = new BlockHitResult(Vec3d.of(pos),
                    direction != null ? mode.is("Strict") && !head ? direction.getOpposite() : direction : Direction.DOWN,
                    mode.is("Strict") && direction != null && !head ? pos.offset(direction) : pos, false);
            mc.player.swingHand(hand);
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(hand, result));
            placedBlocks++;
        }
    }

    private static List<BlockPos> getPositions(int level) {
        return Stream.of(
                new BlockPos(mc.player.getBlockPos().north().getX(), mc.player.getBlockPos().getY() + level, mc.player.getBlockPos().north().getZ()),
                new BlockPos(mc.player.getBlockPos().east().getX(), mc.player.getBlockPos().getY() + level, mc.player.getBlockPos().east().getZ()),
                new BlockPos(mc.player.getBlockPos().south().getX(), mc.player.getBlockPos().getY() + level, mc.player.getBlockPos().south().getZ()),
                new BlockPos(mc.player.getBlockPos().west().getX(), mc.player.getBlockPos().getY() + level, mc.player.getBlockPos().west().getZ()))
                .filter(pos -> mc.world.getBlockState(pos).getMaterial().isReplaceable()).collect(Collectors.toList());
    }

    public boolean hasObsidian() {
        return InventoryUtil.findItemInHotbar(Items.OBSIDIAN) != -1 || mc.player.getOffHandStack().getItem() == Items.OBSIDIAN;
    }
}
