package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.api.util.WorldUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;

@Module.Register(name = "StorageESP", description = "Highlights storages", category = Module.Category.VISUALS)
public class StorageESP extends Module {
    private final SettingGroup chests = new SettingGroup.Builder("Chest").toggleable(true).setup(this);
    private final SettingGroup echests = new SettingGroup.Builder("EnderChest").toggleable(true).setup(this);
    private final SettingGroup shulkers = new SettingGroup.Builder("Shulker").toggleable(true).setup(this);
    private final SettingGroup furnaces = new SettingGroup.Builder("Furnace").toggleable(true).setup(this);
    private final SettingGroup trappedChests = new SettingGroup.Builder("TrappedChest").toggleable(true).setup(this);
    private final SettingGroup dispencers = new SettingGroup.Builder("Dispencer").toggleable(true).setup(this);
    private final SettingGroup droppers = new SettingGroup.Builder("Dropper").toggleable(true).setup(this);
    private final SettingGroup hoppers = new SettingGroup.Builder("Hopper").toggleable(true).setup(this);
    private final SettingGroup itemFrames = new SettingGroup.Builder("ItemFrame").toggleable(true).setup(this);
    private final SettingGroup minecarts = new SettingGroup.Builder("Minecart").toggleable(true).setup(this);

    private final SettingEnum chestsMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(chests);
    private final SettingEnum echestsMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(echests);
    private final SettingEnum shulkersMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(shulkers);
    private final SettingEnum furnacesMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(furnaces);
    private final SettingEnum trappedChestsMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(trappedChests);
    private final SettingEnum dispencersMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(dispencers);
    private final SettingEnum droppersMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(droppers);
    private final SettingEnum hopperMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(hoppers);
    private final SettingEnum itemFrameMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(itemFrames);
    private final SettingEnum minecartMode = new SettingEnum.Builder("Mode").modes("Fill", "Outline").setup(minecarts);

    private final SettingColor chestsC = new SettingColor.Builder("Color").color(new ColorMutable(255, 120, 80, 255)).setup(chests);
    private final SettingColor echestsC = new SettingColor.Builder("Color").color(new ColorMutable(255, 100, 255, 255)).setup(echests);
    private final SettingColor shulkersC = new SettingColor.Builder("Color").color(new ColorMutable(190, 120, 255, 255)).setup(shulkers);
    private final SettingColor furnacesC = new SettingColor.Builder("Color").color(new ColorMutable(119, 119, 119, 255)).setup(furnaces);
    private final SettingColor trappedChestsC = new SettingColor.Builder("Color").color(new ColorMutable(255, 120, 80, 255)).setup(trappedChests);
    private final SettingColor dispencersC = new SettingColor.Builder("Color").color(new ColorMutable(119, 119, 119, 255)).setup(dispencers);
    private final SettingColor droppersC = new SettingColor.Builder("Color").color(new ColorMutable(119, 119, 119, 255)).setup(droppers);
    private final SettingColor hoppersC = new SettingColor.Builder("Color").color(new ColorMutable(234, 43, 0, 255)).setup(hoppers);
    private final SettingColor itemFramesC = new SettingColor.Builder("Color").color(new ColorMutable(255, 120, 80, 255)).setup(itemFrames);
    private final SettingColor minecartsC = new SettingColor.Builder("Color").color(new ColorMutable(255, 120, 80, 255)).setup(minecarts);

    private final SettingNumber lineWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(this);

    private final ArrayList<Entity> entities = new ArrayList<>();

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        for (BlockEntity block : WorldUtil.getBlockEntities()) {
            if (!mc.options.getPerspective().isFirstPerson()) continue;

            if (block instanceof ChestBlockEntity && chests.isToggled()) {
                if (chestsMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, chestsC, Scale.CHEST);
                if (chestsMode.is("Outline"))
                    drawOutline(event.matrixStack, block, chestsC, Scale.CHEST);
            } else if (block instanceof EnderChestBlockEntity && echests.isToggled()) {
                if (echestsMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, echestsC, Scale.CHEST);
                if (echestsMode.is("Outline"))
                    drawOutline(event.matrixStack, block, echestsC, Scale.CHEST);
            } else if (block instanceof ShulkerBoxBlockEntity && shulkers.isToggled()) {
                if (shulkersMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, shulkersC, Scale.FULL);
                if (shulkersMode.is("Outline"))
                    drawOutline(event.matrixStack, block, shulkersC, Scale.FULL);
            } else if (block instanceof AbstractFurnaceBlockEntity && furnaces.isToggled()) {
                if (furnacesMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, furnacesC, Scale.FULL);
                if (furnacesMode.is("Outline"))
                    drawOutline(event.matrixStack, block, furnacesC, Scale.FULL);
            } else if (block instanceof TrappedChestBlockEntity && trappedChests.isToggled()) {
                if (trappedChestsMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, trappedChestsC, Scale.CHEST);
                if (trappedChestsMode.is("Outline"))
                    drawOutline(event.matrixStack, block, trappedChestsC, Scale.CHEST);
            } else if (block instanceof DispenserBlockEntity && dispencers.isToggled()) {
                if (dispencersMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, dispencersC, Scale.FULL);
                if (dispencersMode.is("Outline"))
                    drawOutline(event.matrixStack, block, dispencersC, Scale.FULL);
            } else if (block instanceof DropperBlockEntity && droppers.isToggled()) {
                if (droppersMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, droppersC, Scale.FULL);
                if (droppersMode.is("Outline"))
                    drawOutline(event.matrixStack, block, droppersC, Scale.FULL);
            } else if (block instanceof HopperBlockEntity && hoppers.isToggled()) {
                if (hopperMode.is("Fill"))
                    drawFilledBox(event.matrixStack, block, hoppersC, Scale.FULL);
                if (hopperMode.is("Outline"))
                    drawOutline(event.matrixStack, block, hoppersC, Scale.FULL);
            }
        }
        if (itemFrames.isToggled() || minecarts.isToggled()) {
            findEntities();
            entities.forEach(entity -> {
                final BlockPos pos = entity.getBlockPos();
                if (entity instanceof ItemFrameEntity && itemFrames.isToggled()) {
                    if (itemFrameMode.is("Fill"))
                        RenderUtil.drawFilledBox(event.matrixStack, generateBox(pos), itemFramesC.getColor());
                    if (itemFrameMode.is("Outline"))
                        RenderUtil.drawOutline(event.matrixStack, generateBox(pos), itemFramesC.getColor(), lineWidth.getValue());
                } else if ((entity instanceof ChestMinecartEntity || entity instanceof FurnaceMinecartEntity || entity instanceof HopperMinecartEntity) && minecarts.isToggled()) {
                    if (minecartMode.is("Fill"))
                        RenderUtil.drawFilledBox(event.matrixStack, generateBox(pos), minecartsC.getColor());
                    if (minecartMode.is("Outline"))
                        RenderUtil.drawOutline(event.matrixStack, generateBox(pos), minecartsC.getColor(), lineWidth.getValue());
                }
            });
        }
    }

    private Box generateBox(BlockPos pos) {
        return new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    private void findEntities() {
        entities.clear();
        mc.world.getEntities().forEach(entity -> {
            if ((entity instanceof ItemFrameEntity && itemFrames.isToggled()) ||
                    ((entity instanceof ChestMinecartEntity || entity instanceof FurnaceMinecartEntity || entity instanceof HopperMinecartEntity) && minecarts.isToggled()))
                entities.add(entity);
        });
    }

    @Override
    public void onDisable() {
        entities.clear();
    }

    private void drawFilledBox(MatrixStack stack, BlockEntity block, SettingColor c, Scale type) {
        RenderUtil.drawFilledBox(stack, new Box(
                        block.getPos().getX() + (type == Scale.CHEST ? 0.06 : 0), block.getPos().getY(), block.getPos().getZ() + (type == Scale.CHEST ? 0.06 : 0),
                        block.getPos().getX() + (type == Scale.CHEST ? 0.94 : 1), block.getPos().getY() + (type == Scale.CHEST ? 0.875 : 1), block.getPos().getZ() + (type == Scale.CHEST ? 0.94 : 1)),
                c.getColor());
    }

    private void drawOutline(MatrixStack stack, BlockEntity block, SettingColor c, Scale type) {
        RenderUtil.drawOutline(stack, new Box(
                        block.getPos().getX() + (type == Scale.CHEST ? 0.06 : 0), block.getPos().getY(), block.getPos().getZ() + (type == Scale.CHEST ? 0.06 : 0),
                        block.getPos().getX() + (type == Scale.CHEST ? 0.94 : 1), block.getPos().getY() + (type == Scale.CHEST ? 0.875 : 1), block.getPos().getZ() + (type == Scale.CHEST ? 0.94 : 1)),
                c.getColor(), lineWidth.getValue());
    }

    private enum Scale {
        FULL, CHEST
    }
}
