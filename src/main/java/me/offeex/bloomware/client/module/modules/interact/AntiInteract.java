package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventInteractBlock;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import net.minecraft.block.*;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;

@Module.Register(name = "AntiInteract", description = "Automatically closes screens.", category = Module.Category.INTERACT)
public class AntiInteract extends Module {
    public final SettingGroup blocks = new SettingGroup.Builder("Blocks").setup(this);
    public final SettingBool forcePlace = new SettingBool.Builder("ForcePlace").setup(this);

    public final SettingBool craftingTable = new SettingBool.Builder("CraftingTable").setup(blocks);
    public final SettingBool anvil = new SettingBool.Builder("Anvil").setup(blocks);
    public final SettingBool enchantmentTable = new SettingBool.Builder("EnchantmentTable").setup(blocks);
    public final SettingBool furnace = new SettingBool.Builder("Furnace").setup(blocks);
    public final SettingBool hopper = new SettingBool.Builder("Hopper").setup(blocks);
    public final SettingBool brewingStand = new SettingBool.Builder("BrewingStand").setup(blocks);
    public final SettingBool shulkerBox = new SettingBool.Builder("ShulkerBox").setup(blocks);
    public final SettingBool chest = new SettingBool.Builder("Chest").setup(blocks);
    public final SettingBool enderChest = new SettingBool.Builder("EnderChest").setup(blocks);

    Block currentBlock;

    @Subscribe
    private void onInteractBlock(EventInteractBlock event) {
        currentBlock = mc.world.getBlockState(event.getBlockHitResult().getBlockPos()).getBlock();
        if (!blockCheck()) return;

        if (forcePlace.getValue()) {
            event.setActionResult(mc.player.getStackInHand(event.getHand()).useOnBlock(new ItemUsageContext(mc.player, event.getHand(), event.getBlockHitResult())));
            if (!mc.isInSingleplayer()) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
                mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(event.getHand(), event.getBlockHitResult()));
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
            }
        } else event.setActionResult(ActionResult.PASS);
    }

    public boolean blockCheck() {
        return currentBlock instanceof CraftingTableBlock && craftingTable.getValue()
                || currentBlock instanceof AnvilBlock && anvil.getValue()
                || currentBlock instanceof EnchantingTableBlock && enchantmentTable.getValue()
                || currentBlock instanceof FurnaceBlock && furnace.getValue()
                || currentBlock instanceof BrewingStandBlock && brewingStand.getValue()
                || currentBlock instanceof ShulkerBoxBlock && shulkerBox.getValue()
                || currentBlock instanceof HopperBlock && hopper.getValue()
                || currentBlock instanceof ChestBlock && chest.getValue()
                || currentBlock instanceof EnderChestBlock && enderChest.getValue();
    }

    /**
     * Client side
     *
     * @see me.offeex.bloomware.mixins.MixinPlayerEntity
     */
}
