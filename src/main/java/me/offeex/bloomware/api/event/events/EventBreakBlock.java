package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class EventBreakBlock extends Event {
    private final BlockState state;
    private final BlockPos pos;

    public EventBreakBlock(BlockState state, BlockPos pos) {
        this.state = state;
        this.pos = pos;
    }

    public BlockState getState() {
        return this.state;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}
