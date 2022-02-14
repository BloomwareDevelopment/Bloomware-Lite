package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.LinkedList;

@Module.Register(name = "Armor", description = "Armor hud.", category = Module.Category.HUD)
public class Armor extends Module {
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Vertical", "Horizontal").selected("Horizontal").setup(this);
    private final SettingBool drawMainHand = new SettingBool.Builder("MainHand").value(true).setup(this);
    private final SettingBool drawOffhand = new SettingBool.Builder("Offhand").value(true).setup(this);
    private final LinkedList<ItemStack> items = new LinkedList<ItemStack>();
    private int offset = 0;
    private final int multiplier = 17;

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        offset = 0;
        for (ItemStack stackItem : mc.player.getArmorItems())
            items.add(stackItem);
        Collections.reverse(items);
        for (ItemStack item : items) {
            RenderUtil.drawItem(item, getMode("Horizontal") ? x + (offset * multiplier) : x, getMode("Vertical") ? y + (offset * multiplier) : y, 1, true);
            offset++;
        }
        if (drawMainHand.getValue()) {
            RenderUtil.drawItem(mc.player.getMainHandStack(), getMode("Horizontal") ? x + (offset * multiplier) : x, getMode("Vertical") ? y + (offset * multiplier) : y, 1, true);
            offset++;
        }
        if (drawOffhand.getValue())
            RenderUtil.drawItem(mc.player.getOffHandStack(), getMode("Horizontal") ? x + (offset * multiplier) : x, getMode("Vertical") ? y + (offset * multiplier) : y, 1, true);
        width = getMode("Horizontal") ? 90 : multiplier;
        height = getMode("Vertical") ? 90 : multiplier;
        items.clear();
    }

    public boolean getMode(String s) {
        return mode.is(s);
    }
}
