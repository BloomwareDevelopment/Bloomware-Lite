package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Module.Register(name = "Scaffold", description = "Allows you to place blocks under you", category = Module.Category.INTERACT)
public class Scaffold extends Module {
    private final SettingBool autoSwitch = new SettingBool.Builder("AutoSwitch").value(true).setup(this);
    private final SettingBool rotate = new SettingBool.Builder("Rotate").setup(this);
    private final SettingGroup render = new SettingGroup.Builder("Render").toggleable(true).setup(this);
    private final SettingColor color = new SettingColor.Builder("Color").color(new ColorMutable(0, 0, 255, 100)).setup(render);
    private final SettingEnum renderMode = new SettingEnum.Builder("Mode").modes("Outline", "Fill").selected("Fill").setup(render);
    private final SettingNumber lineWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(render);

    private BlockPos pos = null;
    private boolean prikol;

    @Override
    public void onDisable() {
        Bloomware.rotationManager.reset();
    }

    @Override
    public void onTick() {
        if (mc.player.age % 2 == 0) prikol = false;
        pos = mc.player.getBlockPos().down();
        if (mc.world.getBlockState(pos).isAir() || mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            int oldSlot = mc.player.getInventory().selectedSlot;
            byte slot = InventoryUtil.findItemInHotbar(BlockItem.class);
            Hand hand = InventoryUtil.findHandItem(BlockItem.class);
            if (slot != -1) {
                mc.player.getInventory().selectedSlot = slot;
                if (rotate.getValue()) Bloomware.rotationManager.setRotationBlockPos(pos);
                mc.interactionManager.interactBlock(mc.player, mc.world, hand, new BlockHitResult(Vec3d.of(pos), Direction.DOWN, pos, false));
                mc.player.swingHand(hand);
                prikol = true;
                if (autoSwitch.getValue()) mc.player.getInventory().selectedSlot = oldSlot;
            }
            else Bloomware.rotationManager.reset();
        }
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        if (pos == null || !prikol) return;
        if (renderMode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, new Box(pos), color.getColor());
        else RenderUtil.drawOutline(event.matrixStack, new Box(pos), color.getColor(), lineWidth.getValue());
    }
}
