package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventExplosionVelocity;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.api.event.events.EventPlayerPush;
import me.offeex.bloomware.api.event.events.EventPlayerVelocity;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@Module.Register(name = "Velocity", description = "Changes your velocity.", category = Module.Category.MOTION)
public class Velocity extends Module {
    private final SettingNumber horizontal = new SettingNumber.Builder("Horizontal").value(0.0).min(0.0).max(100.0).inc(1.0).setup(this);
    private final SettingNumber vertical = new SettingNumber.Builder("Vertical").value(0.0).min(0.0).max(100.0).inc(1.0).setup(this);
    public final SettingBool explosions = new SettingBool.Builder("Explosions").value(true).setup(this);
    private final SettingBool noPush = new SettingBool.Builder("NoPush").value(true).setup(this);
    public final SettingBool flowable = new SettingBool.Builder("Flowable").value(false).setup(this);

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        event.setCancelled(event.getPacket() instanceof EntityVelocityUpdateS2CPacket velocity && velocity.getId() == mc.player.getId() && horizontal.getValue() == 0 && vertical.getValue() == 0);
    }

    @Subscribe
    private void onPlayerVelocity(EventPlayerVelocity e) {
        e.setMultX(horizontal.getValue() / 100);
        e.setMultY(vertical.getValue() / 100);
        e.setMultZ(horizontal.getValue() / 100);
    }

    @Subscribe
    private void onExplosionVelocity(EventExplosionVelocity e) {
        e.setMultX(horizontal.getValue() / 100);
        e.setMultY(vertical.getValue() / 100);
        e.setMultZ(horizontal.getValue() / 100);
    }

    @Subscribe
    private void onPlayerPush(EventPlayerPush event) {
        if (noPush.getValue())
            event.setCancelled(true);
        else {
            event.setX(-event.getX() * horizontal.getValue());
            event.setZ(-event.getZ() * horizontal.getValue());
        }
    }
}