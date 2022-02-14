package me.offeex.bloomware.client.module.modules.motion;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.block.AirBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

@Module.Register(name = "Flight", description = "Allows you to fly.", category = Module.Category.MOTION)
public class Flight extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Vanilla", "Static", "Jetpack").selected("Vanilla").setup(this);
    public final SettingNumber speed = new SettingNumber.Builder("Speed").value(1.0).min(0.1).max(10.0).inc(0.1).setup(this);
    private final SettingBool antiKick = new SettingBool.Builder("AntiKick").value(false).setup(this);
    private final SettingNumber kickDelay = new SettingNumber.Builder("KickDelay").value(1).min(1).max(50).inc(1).setup(this);
    private final SettingNumber kickStep = new SettingNumber.Builder("KickStep").value(1).min(1).max(50).inc(1).setup(this);
    private boolean oldMode;

    @Override
    public void onTick() {
        switch (mode.getSelectedStr()) {
            case "Vanilla" -> {
                mc.player.getAbilities().setFlySpeed((float) (speed.getValue() / 11));
                mc.player.getAbilities().flying = true;
            }
            case "Static" -> {
                mc.player.getAbilities().flying = true;
                mc.player.getAbilities().setFlySpeed((float) speed.getValue());
                if (mc.options.keyJump.isPressed()) mc.player.getAbilities().setFlySpeed((float) speed.getValue() / 4);
                if (mc.options.keySneak.isPressed()) mc.player.getAbilities().setFlySpeed((float) speed.getValue() / 4);
                mc.player.setVelocity(0, 0, 0);
            }
            case "Jetpack" -> {
                mc.player.getAbilities().flying = false;
                mc.player.addVelocity(0, (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SPACE)) ?
                        speed.getValue() / 25 : (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) ?
                        -speed.getValue() / 50 : 0, 0);
            }
        }
        if (!mode.is("Jetpack")
                && antiKick.getValue()
                && !mc.player.isTouchingWater()
                && !mc.player.isInLava()
                && mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock() instanceof AirBlock) {
            if (mc.player.age % kickDelay.getValue() == 0) {
                mc.player.setPos(mc.player.getX(), mc.player.getY() - kickStep.getValue() / 500, mc.player.getZ());
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - kickStep.getValue() / 500, mc.player.getZ(), false));
            }
        }
    }

    @Override
    public void onEnable() {
        oldMode = mc.player.getAbilities().flying;
    }

    @Override
    public void onDisable() {
        mc.player.getAbilities().flying = oldMode;
        mc.player.getAbilities().setFlySpeed(0.05f);
    }
}
