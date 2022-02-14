package me.offeex.bloomware.client.module.modules.screen;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.api.event.events.EventKeyRelease;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Module.Register(name = "Freecam", description = "Allows you fly like spectator.", category = Module.Category.CAMERA)
public class Freecam extends Module {
    private final SettingNumber speed = new SettingNumber.Builder("Speed").value(1.0).min(0.1).max(1.0).inc(0.1).setup(this);
    private final SettingBool autoReloadChunks = new SettingBool.Builder("ReloadChunks").value(true).setup(this);

    public OtherClientPlayerEntity fakeman;
    private Perspective oldPerspective;
    private boolean right, up, down, left, forward, backward;
    private Vec3d previousPos, currentPos;

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            disable();
            return;
        }
        right = up = down = backward = left = forward = false;
        double eye = 1.6200000047683716;
        previousPos = mc.player.getPos().add(0, eye, 0);
        currentPos = mc.player.getPos().add(0, eye, 0);
        oldPerspective = mc.options.getPerspective();
        stopMovement();
        fakeman = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
        fakeman.copyFrom(mc.player);
        if (autoReloadChunks.getValue()) mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.world == null) return;
        mc.options.setPerspective(oldPerspective);
        if (autoReloadChunks.getValue()) mc.worldRenderer.reload();
    }

    @Subscribe
    private void onKeyPress(EventKeyPress event) {
        boolean cancel = true;
        if (mc.options.keyJump.matchesKey(event.getKey(), 0)) {
            up = true;
            mc.options.keyJump.setPressed(false);
        } else if (mc.options.keyBack.matchesKey(event.getKey(), 0)) {
            backward = true;
            mc.options.keyBack.setPressed(false);
        } else if (mc.options.keyForward.matchesKey(event.getKey(), 0)) {
            forward = true;
            mc.options.keyForward.setPressed(false);
        } else if (mc.options.keySneak.matchesKey(event.getKey(), 0)) {
            down = true;
            mc.options.keySneak.setPressed(false);
        } else if (mc.options.keyLeft.matchesKey(event.getKey(), 0)) {
            left = true;
            mc.options.keyLeft.setPressed(false);
        } else if (mc.options.keyRight.matchesKey(event.getKey(), 0)) {
            right = true;
            mc.options.keyRight.setPressed(false);
        } else cancel = false;

        if (cancel) event.setCancelled(true);
    }

    @Subscribe
    private void onKeyRelease(EventKeyRelease event) {
        boolean cancel = true;
        if (mc.options.keyJump.matchesKey(event.getKey(), 0)) {
            up = false;
            mc.options.keyJump.setPressed(false);
        } else if (mc.options.keyBack.matchesKey(event.getKey(), 0)) {
            backward = false;
            mc.options.keyBack.setPressed(false);
        } else if (mc.options.keyForward.matchesKey(event.getKey(), 0)) {
            forward = false;
            mc.options.keyForward.setPressed(false);
        } else if (mc.options.keySneak.matchesKey(event.getKey(), 0)) {
            down = false;
            mc.options.keySneak.setPressed(false);
        } else if (mc.options.keyLeft.matchesKey(event.getKey(), 0)) {
            left = false;
            mc.options.keyLeft.setPressed(false);
        } else if (mc.options.keyRight.matchesKey(event.getKey(), 0)) {
            right = false;
            mc.options.keyRight.setPressed(false);
        } else cancel = false;

        if (cancel) event.setCancelled(true);
    }

    @Subscribe
    private void onOpenScreen(EventOpenScreen event) {
        stopMovement();
    }

    private void stopMovement() {
        mc.options.keyJump.setPressed(false);
        mc.options.keyBack.setPressed(false);
        mc.options.keyForward.setPressed(false);
        mc.options.keySneak.setPressed(false);
        mc.options.keyLeft.setPressed(false);
        mc.options.keyRight.setPressed(false);
        right = up = down = backward = left = forward = false;
    }

    @Subscribe
    public void onWorldRender(EventWorldRender event) {
        float tickdelta = event.tickdelta;

        final Vec3d forwardVec = Vec3d.fromPolar(0, fakeman.getYaw());
        final Vec3d rightVec = Vec3d.fromPolar(0, fakeman.getYaw() + 90);
        double velX = 0;
        double velY = 0;
        double velZ = 0;

        boolean a = false;

        if (forward) {
            velX += forwardVec.getX() * speed.getValue();
            velZ += forwardVec.getZ() * speed.getValue();
            a = true;
        }
        if (backward) {
            velX -= forwardVec.getX() * speed.getValue();
            velZ -= forwardVec.getZ() * speed.getValue();
            a = true;
        }

        boolean b = false;
        if (right) {
            velX += rightVec.getX() * speed.getValue();
            velZ += rightVec.getZ() * speed.getValue();
            b = true;
        }
        if (left) {
            velX -= rightVec.getX() * speed.getValue();
            velZ -= rightVec.getZ() * speed.getValue();
            b = true;
        }

        if (a && b) {
            double diagonal = 1 / Math.sqrt(2);
            velX *= diagonal;
            velZ *= diagonal;
        }

        if (up) velY += 1 * speed.getValue();
        if (down) velY -= 1 * speed.getValue();

        if (mc.options.getPerspective() != null) mc.options.setPerspective(Perspective.FIRST_PERSON);
        previousPos = currentPos;
        currentPos = currentPos.add(velX, velY, velZ);
        fakeman.setPosition(mc.gameRenderer.getCamera().getPos());
    }

    public double getX(float tickDelta) {
        return MathHelper.lerp(tickDelta, currentPos.getX(), previousPos.getX());
    }

    public double getY(float tickDelta) {
        return MathHelper.lerp(tickDelta, currentPos.getY(), previousPos.getY());
    }

    public double getZ(float tickDelta) {
        return MathHelper.lerp(tickDelta, currentPos.getZ(), previousPos.getZ());
    }
}
