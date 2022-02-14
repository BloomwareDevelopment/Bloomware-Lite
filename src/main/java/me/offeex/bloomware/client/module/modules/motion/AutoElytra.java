package me.offeex.bloomware.client.module.modules.motion;

import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.WorldUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Direction;

@Module.Register(name = "AutoElytra", description = "Automatically equips elytra if you are falling", category = Module.Category.MOTION)
public class AutoElytra extends Module {
    private final SettingBool equip = new SettingBool.Builder("Equip").value(true).setup(this);
    private final SettingBool takeOff = new SettingBool.Builder("TakeOff").value(true).setup(this);
    private final SettingNumber fallDistance = new SettingNumber.Builder("FallDistance").value(0).min(0).max(100).inc(1).setup(this);
    private final SettingBool autoDisable = new SettingBool.Builder("AutoDisable").value(true).setup(this);

    @Override
    public void onTick() {
        if (mc.player.fallDistance >= fallDistance.getValue()) {
            if (!mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) && equip.getValue()) {
                byte slot = InventoryUtil.findItem(Items.ELYTRA);
                if (slot != -1) InventoryUtil.moveItem(InventoryUtil.slotIndexToId(slot), 6);
                if (autoDisable.getValue()) disable();
            }
            if (!mc.player.isOnGround() && !mc.player.isFallFlying() && !mc.player.isTouchingWater() && takeOff.getValue()) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                if (autoDisable.getValue()) disable();
            }
        }
    }
}
