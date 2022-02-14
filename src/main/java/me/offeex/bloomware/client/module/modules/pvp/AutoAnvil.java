package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Module.Register(name = "AutoAnvil", description = "Places anvils above your enemies", category = Module.Category.PVP)
public class AutoAnvil extends Module {
    private final SettingNumber range = new SettingNumber.Builder("Range").value(3).min(1).max(3).inc(0.1).setup(this);
    private final SettingEnum sort = new SettingEnum.Builder("Sort").modes("Health", "Distance").setup(this);
    private final SettingNumber tickDelay = new SettingNumber.Builder("TickDelay").value(20).min(1).max(100).inc(1).setup(this);
    private final SettingNumber upDistance = new SettingNumber.Builder("UpDistance").value(2).min(1).max(3).inc(1).setup(this);

    private List<PlayerEntity> entities = new ArrayList<>();

    @Override
    public void onTick() {
        sortEntities();
        entities.forEach(entity -> {
            if (shouldAttack(entity) && mc.player.age % tickDelay.getValue() == 0) {
                final BlockPos pos = entity.getBlockPos().up((int) upDistance.getValue());
                byte slot = InventoryUtil.findItemInHotbar(Items.ANVIL);
                if (slot != -1 && mc.world.getBlockState(pos).getBlock() instanceof AirBlock) {
                    mc.player.getInventory().selectedSlot = slot;
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos), Direction.DOWN, pos, false));
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        });
    }

    private void sortEntities() {
        entities.clear();
        mc.world.getEntities().forEach(entity -> {
            if (entity instanceof PlayerEntity) entities.add((PlayerEntity) entity);
        });
        if (sort.is("Health")) entities = entities.stream().sorted(Comparator.comparing(entity -> calculateHealth((PlayerEntity) entity))).collect(Collectors.toList());
        else entities = entities.stream().sorted(Comparator.comparing(entity -> mc.player.distanceTo(entity))).collect(Collectors.toList());
    }

    private boolean shouldAttack(PlayerEntity entity) {
        return mc.player.distanceTo(entity) <= range.getValue() && entity != mc.player;
    }

    private float calculateHealth(PlayerEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }
}
