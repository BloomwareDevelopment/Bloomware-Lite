package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.block.AirBlock;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Module.Register(name = "SelfAnvil", description = "Burrows you with anvil", category = Module.Category.PVP)
public class SelfAnvil extends Module {
    private final SettingBool rotate = new SettingBool.Builder("Rotate").value(false).setup(this);
    private final SettingBool autoSwitch = new SettingBool.Builder("AutoSwitch").value(true).setup(this);

    @Override
    public void onEnable() {
        byte slot = InventoryUtil.findItemInHotbar(Items.ANVIL);
        if (slot == -1) {
            CommandManager.addChatMessage(Formatting.RED + "There are no anvils in your hotbar, disabling!");
        } else {
            final BlockPos pos = mc.player.getBlockPos().up(2);
            int oldSlot = mc.player.getInventory().selectedSlot;
            if (mc.world.getBlockState(pos).getBlock() instanceof AirBlock) {
                mc.player.getInventory().selectedSlot = slot;
                if (rotate.getValue()) Bloomware.rotationManager.setRotationBlockPos(pos);
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos), Direction.DOWN, pos, false));
                mc.player.swingHand(Hand.MAIN_HAND);
                if (rotate.getValue()) Bloomware.rotationManager.reset();
                if (autoSwitch.getValue()) mc.player.getInventory().selectedSlot = oldSlot;
            }
        }
        disable();
    }
}
