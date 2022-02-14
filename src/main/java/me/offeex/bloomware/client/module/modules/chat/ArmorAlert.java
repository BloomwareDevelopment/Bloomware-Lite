package me.offeex.bloomware.client.module.modules.chat;

import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

@Module.Register(name = "ArmorAlert", description = "Notifies you when your armor's durability is low", category = Module.Category.CHAT)
public class ArmorAlert extends Module {
    private final SettingNumber durability = new SettingNumber.Builder("Durability").min(1).max(100).inc(1).value(30).setup(this);
    private final SettingBool sound = new SettingBool.Builder("Sound").value(true).setup(this);

    private boolean helmet, chestplate, legs, boots;

    @Override public void onEnable() {
        helmet = chestplate = legs = boots = false;
    }

    private void warning(ItemStack stack) {
        CommandManager.addChatMessage(Formatting.RED + "Your " + stack.getItem().getName().getString() + " has low durability!");
        if (sound.getValue()) mc.world.playSound(mc.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1f, 1f, true);
    }

    @Override public void onTick() {
        for (ItemStack item : mc.player.getArmorItems()) {
            if (InventoryUtil.getDurability(item) < durability.getValue()) {
                if (InventoryUtil.isHelmet(item) && !helmet) {
                    warning(item);
                    helmet = true;
                }
                if (InventoryUtil.isBoots(item) && !boots) {
                    warning(item);
                    boots = true;
                }
                if (InventoryUtil.isChestplate(item) && !chestplate) {
                    warning(item);
                    chestplate = true;
                }
                if (InventoryUtil.isLegs(item) && !legs) {
                    warning(item);
                    legs = true;
                }
            } else {
                if (InventoryUtil.isLegs(item) && legs) legs = false;
                if (InventoryUtil.isChestplate(item) && chestplate) chestplate = false;
                if (InventoryUtil.isBoots(item) && boots) boots = false;
                if (InventoryUtil.isHelmet(item) && helmet) helmet = false;
            }
        }
    }
}
