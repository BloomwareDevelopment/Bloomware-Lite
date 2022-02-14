package me.offeex.bloomware.client.module.modules.world;

import com.mojang.authlib.GameProfile;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

@Module.Register(name = "FakePlayer", description = "Summons a fake interact entity", category = Module.Category.WORLD)
public class FakePlayer extends Module {
    private OtherClientPlayerEntity player;

    @Override
    public void onEnable() {
        if (mc.world == null && mc.player == null) disable();
        player = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), "TheFakeDiOnFire"));
        player.copyFrom(mc.player);
        mc.world.addEntity(-100, player);
    }

    @Override
    public void onDisable() {
        mc.world.removeEntity(player.getId(), Entity.RemovalReason.DISCARDED);
    }
}
