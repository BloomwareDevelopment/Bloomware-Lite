package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPlayerMove;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.Comparator;

@Module.Register(name = "AutoMount", description = "Automatically mounts entities", category = Module.Category.INTERACT)
public class AutoMount extends Module {
    private final SettingGroup allowed = new SettingGroup.Builder("Allowed").setup(this);
    private final SettingBool boats = new SettingBool.Builder("Boats").value(false).setup(allowed);
    private final SettingBool donkeys = new SettingBool.Builder("Donkeys").value(true).setup(allowed);
    private final SettingBool llamas = new SettingBool.Builder("Llamas").value(true).setup(allowed);
    private final SettingBool pigs = new SettingBool.Builder("Pigs").value(false).setup(allowed);
    private final SettingBool horses = new SettingBool.Builder("Horses").value(true).setup(allowed);
    private final SettingNumber range = new SettingNumber.Builder("Range").min(1).max(5).value(4).setup(this);

    private final ArrayList<Entity> entities = new ArrayList<>();

    @Subscribe
    private void onPlayerMove(EventPlayerMove event) {
        if (mc.player.isRiding() || mc.player.getVehicle() != null) return;
        findEntities();
        Entity selected = entities.stream().min(Comparator.comparing(entity -> mc.player.distanceTo(entity))).orElse(null);
        if (selected != null) mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(selected, false, Hand.MAIN_HAND));
    }

    @Override
    public void onDisable() {
        entities.clear();
    }

    private void findEntities() {
        entities.clear();
        mc.world.getEntities().forEach(entity -> {
            if (isValid(entity)) entities.add(entity);
        });
    }

    private boolean isValid(Entity entity) {
        if (mc.player.distanceTo(entity) > range.getValue()) return false;
        return (entity instanceof PigEntity && pigs.getValue()) || (entity instanceof BoatEntity && boats.getValue()) ||
                (entity instanceof HorseEntity && horses.getValue()) || (entity instanceof DonkeyEntity && donkeys.getValue()) ||
                (entity instanceof LlamaEntity && llamas.getValue());
    }
}
