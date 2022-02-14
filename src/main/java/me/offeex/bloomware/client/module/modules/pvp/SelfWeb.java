package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.MovementUtil;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Module.Register(name = "SelfWeb", description = "Automatically places web on your feet", category = Module.Category.PVP)
public class SelfWeb extends Module {
    private final SettingBool autoSwitch = new SettingBool.Builder("AutoSwitch").value(true).setup(this);
    private final SettingBool autoDisable = new SettingBool.Builder("AutoDisable").value(true).setup(this);
    private final SettingBool autoCenter = new SettingBool.Builder("AutoCenter").value(true).setup(this);
    private final SettingBool rotate = new SettingBool.Builder("Rotate").value(false).setup(this);

    @Override
    public void onEnable() {
        if (InventoryUtil.findItemInHotbar(Items.COBWEB) == -1) {
            CommandManager.addChatMessage(Formatting.RED + "Cobweb was not found in your hotbar, disabling!");
            disable();
        }
    }

    @Override
    public void onTick() {
        byte slot = InventoryUtil.findItemInHotbar(Items.COBWEB);
        int oldSlot = mc.player.getInventory().selectedSlot;

        if (autoCenter.getValue()) MovementUtil.center();
        if (rotate.getValue()) Bloomware.rotationManager.setRotationBlockPos(mc.player.getBlockPos().down());
        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(mc.player.getBlockPos()), Direction.DOWN, mc.player.getBlockPos(), false));
        mc.player.swingHand(Hand.MAIN_HAND);
        if (autoSwitch.getValue()) mc.player.getInventory().selectedSlot = oldSlot;
        if (autoDisable.getValue()) disable();
    }
}
