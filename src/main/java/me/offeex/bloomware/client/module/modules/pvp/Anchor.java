package me.offeex.bloomware.client.module.modules.pvp;

import me.offeex.bloomware.api.util.HoleUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

@Module.Register(name = "Anchor", description = "Stops all motion when you are over a hole", category = Module.Category.PVP)
public class Anchor extends Module {
    private final SettingBool autoDisable = new SettingBool.Builder("AutoDisable").value(true).setup(this);
    private final SettingNumber holeDistance = new SettingNumber.Builder("HoleDistance").min(1).max(3).inc(1).value(1).setup(this);

    @Override
    public void onTick() {
        if (isHole(mc.player.getBlockPos())) {
            mc.player.updatePosition(Math.floor(mc.player.getX()) + 0.5, Math.floor(mc.player.getY()), Math.floor(mc.player.getZ()) + 0.5);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Math.floor(mc.player.getX()) + 0.5, Math.floor(mc.player.getY()), Math.floor(mc.player.getZ()) + 0.5, mc.player.isOnGround()));
            mc.player.setVelocity(0, 0, 0);
            if (autoDisable.getValue()) disable();
        }
    }

    private boolean isHole(BlockPos pos) {
        for (int i = 1; i <= holeDistance.getValue(); i++) {
            if (!HoleUtil.isHole(pos.down(i)).equals(HoleUtil.HoleType.UNSAFE)) return true;
        }
        return false;
    }
}
