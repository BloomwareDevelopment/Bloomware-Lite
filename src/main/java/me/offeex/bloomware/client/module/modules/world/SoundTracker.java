package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;

@Module.Register(name = "SoundTracker", description = "popbob sex dupe.", category = Module.Category.WORLD)
public class SoundTracker extends Module {
    private final SettingBool thunder = new SettingBool.Builder("ThunderSound").value(true).setup(this);
    private final SettingBool endPortal = new SettingBool.Builder("EndPortalSound").value(true).setup(this);

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound() == SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER && thunder.getValue()) {
                CommandManager.addChatMessage("Thunder sound detected -> " + packet.getX() + ", " + packet.getY() + ", " + packet.getZ());
            }
            if (packet.getSound() == SoundEvents.BLOCK_END_PORTAL_SPAWN && endPortal.getValue()) {
                CommandManager.addChatMessage("End portal sound detected -> " + packet.getX() + ", " + packet.getY() + ", " + packet.getZ());
            }
        }
    }
}
