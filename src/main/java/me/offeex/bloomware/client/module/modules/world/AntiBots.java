package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventTick;
import me.offeex.bloomware.api.util.EntityUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Module.Register(name = "AntiBots", description = "Detects and removes bots from the world", category = Module.Category.WORLD)
public class AntiBots extends Module {
    private final SettingGroup targets = new SettingGroup.Builder("Targets").setup(this);
    private final SettingBool invisible = new SettingBool.Builder("Invisible").value(true).setup(targets);
    private final SettingBool gamemode = new SettingBool.Builder("NullGM").value(true).setup(targets);
    private final SettingBool entry = new SettingBool.Builder("NullEntry").value(true).setup(targets);
    private final SettingBool profile = new SettingBool.Builder("NullProfile").value(true).setup(targets);
    private final SettingBool notify = new SettingBool.Builder("Notify").value(true).setup(this);

    @Subscribe private void onTick(EventTick event) {
        for (Entity entity : mc.world.getEntities()) {
            if (isBot(entity)) mc.world.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
        }
    }

    private boolean isBot(Entity entity) {
        return entity instanceof PlayerEntity player &&
                ((EntityUtil.getEntityGamemode(player) == null && gamemode.getValue()) ||
                        (mc.getNetworkHandler().getPlayerListEntry(player.getUuid()) == null && entry.getValue()) ||
                        (mc.getNetworkHandler().getPlayerListEntry(player.getUuid()).getProfile() == null) && profile.getValue() ||
                        (player.isInvisible() && invisible.getValue()));
    }
}
