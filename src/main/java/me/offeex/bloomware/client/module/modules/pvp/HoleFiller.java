package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.api.util.HoleUtil;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.WorldUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

@Module.Register(name = "HoleFiller", description = "Automatically fills safe holes", category = Module.Category.PVP)
public class HoleFiller extends Module {
    private final SettingNumber range = new SettingNumber.Builder("Range").value(4).min(1).max(5).inc(0.1).setup(this);
    private final SettingNumber delay = new SettingNumber.Builder("Delay").value(2).min(1).max(20).inc(1).setup(this);
    private final SettingBool autoSwitch = new SettingBool.Builder("AutoSwitch").value(true).setup(this);
    private final SettingBool autoDisable = new SettingBool.Builder("AutoDisable").value(true).setup(this);

    private final ArrayList<HoleUtil.Hole> holes = new ArrayList<>();

    @Override
    public void onTick() {
        WorldUtil.findHoles(holes, range, mc.player);
        for (HoleUtil.Hole hole : holes) {
            if (mc.player.age % delay.getValue() == 0) {
                if (autoSwitch.getValue()) {
                    byte slot = InventoryUtil.findItemInHotbar(BlockItem.class);
                    if (slot != -1) mc.player.getInventory().selectedSlot = slot;
                }
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(hole.pos()), Direction.DOWN, hole.pos(), false));
                mc.player.swingHand(Hand.MAIN_HAND);
                break;
            }
        }
        if (autoDisable.getValue() && holes.isEmpty()) disable();
    }
}
