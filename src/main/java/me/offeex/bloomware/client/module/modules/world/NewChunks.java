package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

@Module.Register(name = "NewChunks", description = "Shows new generated chunks.", category = Module.Category.WORLD)
public class NewChunks extends Module {
    private final SettingGroup outline = new SettingGroup.Builder("Outline").toggleable(true).toggled(true).setup(this);
    private final SettingColor outlineColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 0, 0, 255)).setup(outline);
    private final SettingNumber width = new SettingNumber.Builder("Width").min(0.1).max(3).inc(0.1).value(1).setup(outline);
    private final SettingGroup fill = new SettingGroup.Builder("Fill").toggleable(true).toggled(false).setup(this);
    private final SettingColor fillColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 0, 0, 100)).setup(fill);
    private final SettingNumber yOffset = new SettingNumber.Builder("YOffset").min(-200).max(250).inc(1).value(-50).setup(this);
    private final SettingNumber range = new SettingNumber.Builder("Range").min(100).max(1000).inc(1).value(500).setup(this);
    private final SettingGroup cleaning = new SettingGroup.Builder("Cleaning").setup(this);
    private final SettingBool afterDisable = new SettingBool.Builder("AfterDisable").value(false).setup(cleaning);
    private final SettingBool afterDistance = new SettingBool.Builder("AfterDistance").value(false).setup(cleaning);

    private final ArrayList<Chunk> chunks = new ArrayList<>();

    @Override public void onDisable() {
        if (afterDisable.getValue()) chunks.clear();
    }

    @Subscribe private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof ChunkDeltaUpdateS2CPacket packet) {
            packet.visitUpdates((pos, state) -> {
                ChunkPos chunkPos = new ChunkPos(pos);
                if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill() && !in(chunkPos.getStartPos().getX(), chunkPos.getStartPos().getZ())) chunks.add(new Chunk(chunkPos));
            });
        }
    }

    private boolean in(int x, int z) {
        return chunks.stream().anyMatch(chunk -> chunk.pos().getStartPos().getX() == x && chunk.pos().getStartPos().getZ() == z);
    }

    @Subscribe private void onWorldRender(EventWorldRender event) {
        try {
            for (Chunk chunk : chunks) {
                if (Math.sqrt(mc.player.squaredDistanceTo(new Vec3d(chunk.pos().getStartPos().getX(), chunk.pos().getStartPos().getY(), chunk.pos().getStartPos().getZ()))) > range.getValue()) {
                    if (afterDistance.getValue()) chunks.remove(chunk);
                    continue;
                }
                if (fill.isToggled()) RenderUtil.drawFilledBox(event.matrixStack, new Box(chunk.pos().getStartPos().down(-((int) yOffset.getValue())), chunk.pos().getStartPos().add(16, yOffset.getValue(), 16)), fillColor.getColor());
                if (outline.isToggled()) RenderUtil.drawOutline(event.matrixStack, new Box(chunk.pos().getStartPos().down(-((int) yOffset.getValue())), chunk.pos().getStartPos().add(16, yOffset.getValue(), 16)), outlineColor.getColor(), width.getValue());
            }
        } catch (ConcurrentModificationException e) {
            return;
        }
    }

    private record Chunk(ChunkPos pos) { }
}
