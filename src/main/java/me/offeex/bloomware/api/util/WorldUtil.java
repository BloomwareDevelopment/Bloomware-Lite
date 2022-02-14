package me.offeex.bloomware.api.util;

import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import static me.offeex.bloomware.Bloomware.mc;

import java.util.*;

public class WorldUtil {
    public static List<WorldChunk> getLoadedChunks() {
        List<WorldChunk> chunks = new ArrayList<>();
        int viewDist = mc.options.viewDistance;

        for (int x = -viewDist; x <= viewDist; x++) {
            for (int z = -viewDist; z <= viewDist; z++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk((int) mc.player.getX() / 16 + x, (int) mc.player.getZ() / 16 + z);
                if (chunk != null) chunks.add(chunk);
            }
        }
        return chunks;
    }

    public static List<BlockEntity> getBlockEntities() {
        List<BlockEntity> list = new ArrayList<>();
        getLoadedChunks().forEach(c -> list.addAll(c.getBlockEntities().values()));
        return list;
    }

    public static void findHoles(ArrayList<HoleUtil.Hole> holes, SettingNumber range, ClientPlayerEntity player) {
        holes.clear();
        for (byte x = (byte) -range.getValue(); x <= (byte) range.getValue(); x++) {
            for (byte y = (byte) -range.getValue(); y <= (byte) range.getValue(); y++) {
                for (byte z = (byte) -range.getValue(); z <= (byte) range.getValue(); z++) {
                    BlockPos pos = new BlockPos(player.getX() + x, player.getY() + y, player.getZ() + z);
                    HoleUtil.HoleType type = HoleUtil.isHole(pos);
                    if (type != HoleUtil.HoleType.UNSAFE) holes.add(new HoleUtil.Hole(pos, type, mc.player.squaredDistanceTo(Vec3d.of(pos))));
                }
            }
        }
        Collections.sort(holes, Comparator.comparing(HoleUtil.Hole::distance));
    }

    public static Block checkBlock(Direction dir) {
        return mc.world.getBlockState(mc.player.getBlockPos().offset(dir, 1)).getBlock();
    }

    public static boolean isPlaceable(BlockPos pos, boolean entityCheck) {
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) return false;
        for (Entity e : mc.world.getEntitiesByClass(Entity.class, new Box(pos), e -> !(e instanceof ExperienceBottleEntity || e instanceof ItemEntity || e instanceof ExperienceOrbEntity))) {
            if (e instanceof PlayerEntity) return false;
            return !entityCheck;
        }
        return true;
    }

    public static Direction getBlockSide(BlockPos pos) {
        for (Direction d : Direction.values())
            if (!mc.world.getBlockState(pos.add(d.getVector())).getMaterial().isReplaceable()) return d;
        return null;
    }

}
