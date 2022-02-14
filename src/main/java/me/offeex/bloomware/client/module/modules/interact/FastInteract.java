package me.offeex.bloomware.client.module.modules.interact;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import me.offeex.bloomware.mixins.accessors.IMinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;

@Module.Register(name = "FastInteract", description = "", category = Module.Category.INTERACT)
public class FastInteract extends Module {
    private final SettingGroup place = new SettingGroup.Builder("Place").toggleable(true).toggled(true).setup(this);
    private final SettingNumber placeDelay = new SettingNumber.Builder("Delay").min(0).max(1).inc(0.25).setup(place);
    private final SettingGroup use = new SettingGroup.Builder("Use").toggleable(true).toggled(true).setup(this);
    private final SettingNumber useDelay = new SettingNumber.Builder("Delay").min(0).max(1).inc(0.25).setup(use);

    @Subscribe
    public void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket && place.isToggled())
            ((IMinecraftClient) mc).setItemUseCooldown((int) (placeDelay.getValue() * 4));

        if (event.getPacket() instanceof PlayerInteractItemC2SPacket && use.isToggled())
            ((IMinecraftClient) mc).setItemUseCooldown((int) (useDelay.getValue() * 4));
    }
}
