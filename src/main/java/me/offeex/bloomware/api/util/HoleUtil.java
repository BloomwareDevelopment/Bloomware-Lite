package me.offeex.bloomware.api.util;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import static me.offeex.bloomware.Bloomware.mc;

public class HoleUtil {
    public static HoleType isHole(BlockPos pos) {
        if (getBlock(pos) instanceof AirBlock) {
            if (getBlock(pos.north()) instanceof AirBlock || getBlock(pos.south()) instanceof AirBlock || getBlock(pos.east()) instanceof AirBlock || getBlock(pos.west()) instanceof AirBlock) return HoleType.UNSAFE;
            byte obsidian = 0, bedrock = 0;
            if (!(getBlock(pos.up()) instanceof AirBlock) || getBlock(pos.down()) instanceof AirBlock) return HoleType.UNSAFE;
            if (getBlock(pos.north()) == Blocks.BEDROCK) bedrock++;
            if (getBlock(pos.north()) == Blocks.OBSIDIAN) obsidian++;
            if (getBlock(pos.east()) == Blocks.BEDROCK) bedrock++;
            if (getBlock(pos.east()) == Blocks.OBSIDIAN) obsidian++;
            if (getBlock(pos.west()) == Blocks.BEDROCK) bedrock++;
            if (getBlock(pos.west()) == Blocks.OBSIDIAN) obsidian++;
            if (getBlock(pos.south()) == Blocks.BEDROCK) bedrock++;
            if (getBlock(pos.south()) == Blocks.OBSIDIAN) obsidian++;
            if (bedrock + obsidian == 4) return bedrock == 4 ? HoleType.BEDROCK : HoleType.SAFE;
        }
        return HoleType.UNSAFE;
    }

    private static Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public enum HoleType {
        BEDROCK, SAFE, UNSAFE;
    }

    public record Hole(BlockPos pos, HoleType type, double distance) {}
}
