package me.offeex.bloomware.api.util;

import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import static me.offeex.bloomware.Bloomware.mc;

public class InventoryUtil {

    public static byte findItem(Item item) {
        for (byte i = 0; i <= mc.player.getInventory().size(); i++)
            if (mc.player.getInventory().getStack(i).getItem().equals(item)) return i;
        return -1;
    }

    public static byte findItem(Class<? extends Item> item) {
        for (byte i = 0; i <= mc.player.getInventory().size(); i++)
            if (mc.player.getInventory().getStack(i).getItem().getClass().equals(item)) return i;
        return -1;
    }

    public static byte findItemInHotbar(Item item) {
        for (byte i = 0; i < 9; i++)
            if (mc.player.getInventory().getStack(i).getItem().equals(item)) return i;
        return -1;
    }

    public static byte findItemInHotbar(Class<? extends Item> item) {
        for (byte i = 0; i < 9; i++)
            if (mc.player.getInventory().getStack(i).getItem().getClass().equals(item)) return i;
        return -1;
    }

    public static Hand findHandItem(Item item) {
        return mc.player.getOffHandStack().getItem() == item ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }

    public static Hand findHandItem(Class<? extends Item> item) {
        return mc.player.getOffHandStack().getItem().getClass().equals(item) ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }


    public static int slotIndexToId(int index) {
        return (index >= 0 && index < 9) ? 36 + index : index;
    }

    public static void moveItem(int oldSlot, int newSlot) {
        mc.interactionManager.clickSlot(0, oldSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(0, newSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(0, oldSlot, 0, SlotActionType.PICKUP, mc.player);
    }

    public static double getDurability(ItemStack stack) {
        return ((double) (stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage()) * 100;
    }

    public static boolean isHelmet(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() == Items.LEATHER_HELMET || itemStack.getItem() == Items.DIAMOND_HELMET || itemStack.getItem() == Items.GOLDEN_HELMET || itemStack.getItem() == Items.IRON_HELMET || itemStack.getItem() == Items.NETHERITE_HELMET || itemStack.getItem() == Items.CHAINMAIL_HELMET;
    }

    public static boolean isChestplate(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.NETHERITE_CHESTPLATE || itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.IRON_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE;
    }

    public static boolean isLegs(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() == Items.LEATHER_LEGGINGS || itemStack.getItem() == Items.NETHERITE_LEGGINGS || itemStack.getItem() == Items.DIAMOND_LEGGINGS || itemStack.getItem() == Items.GOLDEN_LEGGINGS || itemStack.getItem() == Items.IRON_LEGGINGS || itemStack.getItem() == Items.CHAINMAIL_LEGGINGS;
    }

    public static boolean isBoots(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() == Items.LEATHER_BOOTS || itemStack.getItem() == Items.NETHERITE_BOOTS || itemStack.getItem() == Items.DIAMOND_BOOTS || itemStack.getItem() == Items.GOLDEN_BOOTS || itemStack.getItem() == Items.IRON_BOOTS || itemStack.getItem() == Items.CHAINMAIL_BOOTS;
    }
}
