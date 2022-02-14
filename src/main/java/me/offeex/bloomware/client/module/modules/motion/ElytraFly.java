package me.offeex.bloomware.client.module.modules.motion;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventBeginRenderTick;
import me.offeex.bloomware.api.event.events.EventPacket;
import me.offeex.bloomware.api.event.events.EventPlayerTravel;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.api.util.MovementUtil;
import me.offeex.bloomware.api.util.TimerUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import me.offeex.bloomware.mixins.accessors.IPlayerMoveC2SPacket;
import me.offeex.bloomware.mixins.accessors.IPlayerPositionLookS2CPacket;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

@Module.Register(name = "ElytraFly", description = "Allows you to fly using elytra almost without fireworks.", category = Module.Category.MOTION)
public class ElytraFly extends Module {

    public final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Legit", "Packet", "Boost", "Control").selected("Legit").setup(this);

    //    Legit mode
    private final SettingNumber minY = new SettingNumber.Builder("MinY").value(70).min(1).max(100).inc(1).setup(this).depend(() -> mode.is("Legit"), mode);
    private final SettingNumber maxY = new SettingNumber.Builder("MaxY").value(120).min(2).max(300).inc(1).setup(this).depend(() -> mode.is("Legit"), mode);
    private final SettingBool useFireworks = new SettingBool.Builder("UseFireworks").setup(this).depend(() -> mode.is("Legit"), mode);

    //    Packet mode
    private final SettingNumber minSpeed = new SettingNumber.Builder("MinSpeed").value(0.34).min(0.10).max(5.00).inc(0.01).setup(this);
    private final SettingBool antiKick = new SettingBool.Builder("AntiKick").value(true).setup(this).depend(() -> mode.is("Packet"), mode);
    private final SettingBool accelerate = new SettingBool.Builder("Accelerate").value(true).setup(this).depend(() -> mode.is("Packet"), mode);
    private final SettingNumber accelerateMin = new SettingNumber.Builder("AccelerateMin").min(0.0).max(1).inc(0.1).value(0.1).setup(this).depend(() -> mode.is("Packet"), mode);
    private final SettingNumber accelerateSpeed = new SettingNumber.Builder("AccelerateSpeed").min(0.1).max(1).inc(0.1).value(0.1).setup(this).depend(() -> mode.is("Packet"), mode);

    //    Boost mode
    private final SettingNumber boost = new SettingNumber.Builder("Boost").min(0).max(1).inc(0.01).value(0.05).setup(this).depend(() -> mode.is("Boost"), mode);
    private final SettingNumber maxBoost = new SettingNumber.Builder("MaxBoost").min(0).max(5).inc(0.01).value(2.5).setup(this).depend(() -> mode.is("Boost"), mode);

    int b = 0;
    double acceleratedProgress;

    @Subscribe
    private void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket.Full packet && isPacketEFlying())
            ((IPlayerMoveC2SPacket) packet).setPitch(.5f); /* For Packet mode */
    }

    @Subscribe
    private void onPacketReceive(EventPacket.Receive event) {
        if (mc.player.isSpectator() || !getChest().isOf(Items.ELYTRA) || getElytraDurability() <= 1 || !mc.player.getAbilities().flying)
            return;

        /* This supposed to cancel FallFlying animation */
        if (event.getPacket() instanceof EntityTrackerUpdateS2CPacket packet && isPacketEFlying()) {
            if (packet.id() == mc.player.getId()) event.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        Bloomware.rotationManager.reset();
    }

    /*
     * Instead of "onTick"
     */
    @Subscribe
    private void onPlayerTravel(EventPlayerTravel event) {
        if (!getChest().isOf(Items.ELYTRA) || getElytraDurability() < 1) return;

        switch (mode.getSelectedStr()) {
            case "Legit" -> {
                if (!mc.player.isFallFlying()) {
                    Bloomware.rotationManager.reset();
                    return;
                }
                if (mc.player.getY() <= minY.getValue()) {
                    mc.player.setPitch(-40.5F);
                }
                b++;
                if (useFireworks.getValue() && b == 1) {
                    int slotFirework = InventoryUtil.findItemInHotbar(Items.FIREWORK_ROCKET);
                    if (slotFirework != -1) {
                        mc.player.getInventory().selectedSlot = slotFirework;
                        Objects.requireNonNull(mc.interactionManager).interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                    }
                }
            }
            case "Packet" -> {
                if (isPacketEFlying()) eFlyPacket();
            }
            case "Boost" -> {
                double yawRadian = Math.toRadians(mc.player.getYaw());
                if (mc.player.isFallFlying() && MovementUtil.getVelocity(mc.player) <= maxBoost.getValue()) {
                    if (mc.options.keyForward.isPressed() && mc.player.getPitch() > -3)
                        mc.player.addVelocity(Math.sin(yawRadian) * -boost.getValue(), 0, Math.cos(yawRadian) * boost.getValue());
                    else if (mc.options.keyBack.isPressed())
                        mc.player.addVelocity(Math.sin(yawRadian) * boost.getValue(), 0, Math.cos(yawRadian) * -boost.getValue());
                }
            }
        }
    }

    public void eFlyPacket() {
        if (mc.player.isOnGround()) return;

        if (mc.player.age % 7 == 0 && antiKick.getValue())
            mc.player.setVelocity(mc.player.getVelocity().withAxis(Direction.Axis.Y, -0.00001f));

        Input input = mc.player.input;
        if (input.movementSideways == 0 && input.movementForward == 0)
            acceleratedProgress = accelerateMin.getValue();
        else {
            acceleratedProgress = acceleratedProgress + accelerateSpeed.getValue() / 5;
            if (acceleratedProgress > 1) acceleratedProgress = 1;
        }

        if (!shouldHold()) mc.player.setVelocity(getMotionOnKey(minSpeed.getValue()));
        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        mc.player.fallDistance = 0;
    }

    private Vec3d getMotionOnKey(double speed) {
        return getMotionOnKey((float) speed, (float) speed);
    }

    private Vec3d getMotionOnKey(double hSpeed, double vSpeed) {
        Input input = mc.player.input;
        float movementVertical = input.jumping != input.sneaking ? input.jumping ? 1.0f : -1.0f : 0;
        float yaw = mc.player.getYaw();
        float forward = input.movementForward;
        float strafe = input.movementSideways;
        if (forward != 0) {
            if (strafe > 0.0f) yaw += ((forward > 0.0f) ? -45 : 45);
            else if (strafe < 0.0f) yaw += ((forward > 0.0f) ? 45 : -45);

            strafe = 0.0f;
            if (forward > 0.0f) forward = 1;
            else if (forward < 0.0f) forward = -1;
        }
        if (strafe > 0.0f) strafe = 1;
        else if (strafe < 0) strafe = -1;

        float yawRad = (float) Math.toRadians(yaw + 90.0f);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        return new Vec3d(
                hSpeed * (forward * cos + strafe * sin),
                vSpeed * movementVertical,
                hSpeed * (forward * sin - strafe * cos));
    }

    private boolean shouldHold() {
        Input input = mc.player.input;
        GameOptions options = mc.options;
        return mc.player.input.movementForward == 0 && input.movementSideways == 0 && !options.keyJump.isPressed() && !options.keySneak.isPressed() && !mc.player.isOnGround();
    }

    private boolean isPacketEFlying() {
        return mode.is("Packet") && mc.player != null && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
    }

    private double packetSpeed() {
        return accelerate.getValue() ? minSpeed.getValue() * acceleratedProgress : minSpeed.getValue();
    }

    private int getElytraDurability() {
        return getChest().getMaxDamage() - getChest().getDamage();
    }

    private ItemStack getChest() {
        return mc.player.getEquippedStack(EquipmentSlot.CHEST);
    }
}
