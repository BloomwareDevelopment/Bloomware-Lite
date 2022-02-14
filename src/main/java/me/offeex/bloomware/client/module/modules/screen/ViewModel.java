package me.offeex.bloomware.client.module.modules.screen;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventHeldItemRenderer;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3f;

@Module.Register(name = "ViewModel", description = "Changes your hand renderer", category = Module.Category.CAMERA)
public class ViewModel extends Module {
    private final SettingGroup mainHand = new SettingGroup.Builder("MainHand").setup(this);
    private final SettingGroup offHand = new SettingGroup.Builder("OffHand").setup(this);

    private final SettingGroup scaleMain = new SettingGroup.Builder("Scale").setup(mainHand);
    private final SettingGroup positionMain = new SettingGroup.Builder("Position").setup(mainHand);
    private final SettingGroup rotationMain = new SettingGroup.Builder("Rotation").setup(mainHand);
    private final SettingGroup animateMain = new SettingGroup.Builder("Animate").setup(mainHand);
    private final SettingGroup scaleOff = new SettingGroup.Builder("Scale").setup(offHand);
    private final SettingGroup positionOff = new SettingGroup.Builder("Position").setup(offHand);
    private final SettingGroup rotationOff = new SettingGroup.Builder("Rotation").setup(offHand);
    private final SettingGroup animateOff = new SettingGroup.Builder("Animate").setup(offHand);

    private final SettingNumber scaleMainX = new SettingNumber.Builder("ScaleX").value(1.00).min(0.10).max(5.00).inc(0.01).setup(scaleMain);
    private final SettingNumber scaleMainY = new SettingNumber.Builder("ScaleY").value(1.00).min(0.10).max(5.00).inc(0.01).setup(scaleMain);
    private final SettingNumber scaleMainZ = new SettingNumber.Builder("ScaleZ").value(1.00).min(0.10).max(5.00).inc(0.01).setup(scaleMain);
    private final SettingNumber scaleOffX = new SettingNumber.Builder("ScaleZ").value(1.00).min(0.10).max(5.00).inc(0.01).setup(scaleOff);
    private final SettingNumber scaleOffY = new SettingNumber.Builder("ScaleY").value(1.00).min(0.10).max(5.00).inc(0.01).setup(scaleOff);
    private final SettingNumber scaleOffZ = new SettingNumber.Builder("ScaleZ").value(1.00).min(0.10).max(5.00).inc(0.01).setup(scaleOff);

    private final SettingNumber positionMainX = new SettingNumber.Builder("PositionX").value(0.00).min(-3.00).max(3.00).inc(0.01).setup(positionMain);
    private final SettingNumber positionMainY = new SettingNumber.Builder("PositionY").value(0.00).min(-3.00).max(3.00).inc(0.01).setup(positionMain);
    private final SettingNumber positionMainZ = new SettingNumber.Builder("PositionZ").value(0.00).min(-3.00).max(3.00).inc(0.01).setup(positionMain);
    private final SettingNumber positionOffX = new SettingNumber.Builder("PositionX").value(0.00).min(-3.00).max(3.00).inc(0.01).setup(positionOff);
    private final SettingNumber positionOffY = new SettingNumber.Builder("PositionY").value(0.00).min(-3.00).max(3.00).inc(0.01).setup(positionOff);
    private final SettingNumber positionOffZ = new SettingNumber.Builder("PositionZ").value(0.00).min(-3.00).max(3.00).inc(0.01).setup(positionOff);

    private final SettingNumber rotationMainX = new SettingNumber.Builder("RotationX").value(0.0).min(-180.0).max(180.0).inc(1.0).setup(rotationMain);
    private final SettingNumber rotationMainY = new SettingNumber.Builder("RotationY").value(0.0).min(-180.0).max(180.0).inc(1.0).setup(rotationMain);
    private final SettingNumber rotationMainZ = new SettingNumber.Builder("RotationZ").value(0.0).min(-180.0).max(180.0).inc(1.0).setup(rotationMain);
    private final SettingNumber rotationOffX = new SettingNumber.Builder("RotationX").value(0.0).min(-180.0).max(180.0).inc(1.0).setup(rotationOff);
    private final SettingNumber rotationOffY = new SettingNumber.Builder("RotationY").value(0.0).min(-180.0).max(180.0).inc(1.0).setup(rotationOff);
    private final SettingNumber rotationOffZ = new SettingNumber.Builder("RotationZ").value(0.0).min(-180.0).max(180.0).inc(1.0).setup(rotationOff);

    private final SettingBool animateMainX = new SettingBool.Builder("AnimateX").value(true).setup(animateMain);
    private final SettingBool animateMainY = new SettingBool.Builder("AnimateY").value(false).setup(animateMain);
    private final SettingBool animateMainZ = new SettingBool.Builder("AnimateZ").value(false).setup(animateMain);
    private final SettingNumber speedAnimateMain = new SettingNumber.Builder("Speed").min(1).max(5).setup(animateMain);

    private final SettingBool animateOffX = new SettingBool.Builder("AnimateX").value(true).setup(animateOff);
    private final SettingBool animateOffY = new SettingBool.Builder("AnimateY").value(false).setup(animateOff);
    private final SettingBool animateOffZ = new SettingBool.Builder("AnimateZ").value(false).setup(animateOff);
    private final SettingNumber speedAnimateOff = new SettingNumber.Builder("Speed").min(1).max(5).setup(animateOff);

    public final SettingBool oldAnimations = new SettingBool.Builder("OldAnimations").value(false).setup(this);

    private double changeRotate(double value, double speed) {
        return value - speed <= 180 && value - speed > -180 ? value - speed : 180;
    }

    @Subscribe
    private void onHeldItemRender(EventHeldItemRenderer event) {
        if (!isEnabled()) return;

        if (event.getHand() == Hand.MAIN_HAND) {
            if (animateMainX.getValue()) rotationMainX.setValue(changeRotate(rotationMainX.getValue(), speedAnimateMain.getValue()));
            if (animateMainY.getValue()) rotationMainY.setValue(changeRotate(rotationMainY.getValue(), speedAnimateMain.getValue()));
            if (animateMainZ.getValue()) rotationMainZ.setValue(changeRotate(rotationMainZ.getValue(), speedAnimateMain.getValue()));
            event.getStack().multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) rotationMainX.getValue()));
            event.getStack().multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) rotationMainY.getValue()));
            event.getStack().multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) rotationMainZ.getValue()));
            event.getStack().scale((float) scaleMainX.getValue(), (float) scaleMainY.getValue(), (float) scaleMainZ.getValue());
            event.getStack().translate(positionMainX.getValue(), positionMainY.getValue(), positionMainZ.getValue());
        } else {
            if (animateOffX.getValue()) rotationOffX.setValue(changeRotate(rotationOffX.getValue(), speedAnimateOff.getValue()));
            if (animateOffY.getValue()) rotationOffY.setValue(changeRotate(rotationOffY.getValue(), speedAnimateOff.getValue()));
            if (animateOffZ.getValue()) rotationOffZ.setValue(changeRotate(rotationOffZ.getValue(), speedAnimateOff.getValue()));
            event.getStack().multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) rotationOffX.getValue()));
            event.getStack().multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) rotationOffY.getValue()));
            event.getStack().multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) rotationOffZ.getValue()));
            event.getStack().scale((float) scaleOffX.getValue(), (float) scaleOffY.getValue(), (float) scaleOffZ.getValue());
            event.getStack().translate(positionOffX.getValue(), positionOffY.getValue(), positionOffZ.getValue());
        }
    }
}
