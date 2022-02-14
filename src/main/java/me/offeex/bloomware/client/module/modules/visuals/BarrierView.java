package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.BarrierBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;

@Module.Register(name = "BarrierView", description = "Renders barrier blocks", category = Module.Category.VISUALS)
public class BarrierView extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").selected("Fill").setup(this);
    private final SettingColor color = new SettingColor.Builder("Color").color(new ColorMutable(255, 0, 0, 255)).setup(this);
    private final SettingNumber range = new SettingNumber.Builder("Range").min(4).max(30).inc(1).value(6).setup(this);
    private final SettingNumber lineWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(this);

    private final ArrayList<BlockPos> barriers = new ArrayList<>();

    private void findBarriers() {
        barriers.clear();
        for (byte x = (byte) -range.getValue(); x <= (byte) range.getValue(); x++) {
            for (byte y = (byte) -range.getValue(); y <= (byte) range.getValue(); y++) {
                for (byte z = (byte) -range.getValue(); z <= (byte) range.getValue(); z++) {
                    BlockPos pos = new BlockPos(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
                    if (mc.world.getBlockState(pos).getBlock() instanceof BarrierBlock) barriers.add(pos);
                }
            }
        }
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        findBarriers();
        barriers.forEach(barrier -> {
            if (mode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, new Box(barrier.getX(), barrier.getY(), barrier.getZ(),
                    barrier.getX() + 1, barrier.getY() + 1, barrier.getZ() + 1), color.getColor());
            else if (mode.is("Outline")) RenderUtil.drawOutline(event.matrixStack, new Box(barrier.getX(), barrier.getY(), barrier.getZ(),
                    barrier.getX() + 1, barrier.getY() + 1, barrier.getZ() + 1), color.getColor(), lineWidth.getValue());
        });
    }
}
