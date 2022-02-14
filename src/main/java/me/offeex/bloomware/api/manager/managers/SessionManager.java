package me.offeex.bloomware.api.manager.managers;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.api.event.events.EventPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.text.DecimalFormat;

public class SessionManager {
    private int pops = 0, deaths = 0, kills = 0;
    private long timeOnline;
    private float tps;
    private double lastTimePacketReceived;
    private Packet<?> packet;

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (packet.getStatus() == 35 && packet.getEntity(MinecraftClient.getInstance().world) == MinecraftClient.getInstance().player) pops++;
        }
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            int tmp = (int) Math.round(calculateTps(System.currentTimeMillis() - lastTimePacketReceived) * 100);
            tps = tmp / 100f;
        }
        lastTimePacketReceived = System.currentTimeMillis();
        packet = event.getPacket();
    }

    @Subscribe
    private void onScreenOpen(EventOpenScreen event) {
        if (event.getScreen() instanceof DeathScreen) deaths++;
    }

    public void addDeath() {
        deaths++;
    }

    public void reset() {
        pops = 0;
        deaths = 0;
        kills = 0;
        timeOnline = 0;
    }

    public void start() {
        Bloomware.EVENTBUS.register(this);
        timeOnline = System.currentTimeMillis();
    }

    public boolean isLagging() {
        return System.currentTimeMillis() - lastTimePacketReceived > 3000;
    }

    public int getPops() {
        return pops;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public float getTps() {
        return tps;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public long getTimeOnline() {
        return (System.currentTimeMillis() - timeOnline) / 1000;
    }

    public String convertTime(long time) {
        return String.format("%s hours, %s minutes, %s seconds", time / 3600, (time - (time / 3600 * 3600) - (time % 60)) / 60, time % 60);
    }

    private double calculateTps(double n) {
        return (20.0 / Math.max((n - 1000.0) / (500.0), 1.0));
    }
}
