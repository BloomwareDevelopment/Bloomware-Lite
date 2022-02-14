package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;

import java.util.*;

@Module.Register(name = "PvPInfo", description = "", category = Module.Category.HUD)
public class PvPInfo extends Module {
    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        String obsidian = "Obsidian: " + mc.player.getInventory().count(Items.OBSIDIAN);
        String crystals = "Crystals: " + mc.player.getInventory().count(Items.END_CRYSTAL);
        String exp = "Exp: " + mc.player.getInventory().count(Items.EXPERIENCE_BOTTLE);
        String gapples = "Gapples: " + mc.player.getInventory().count(Items.ENCHANTED_GOLDEN_APPLE);
        String totems = "Totems: " + mc.player.getInventory().count(Items.TOTEM_OF_UNDYING);

        String[] strings = {obsidian, crystals, exp, gapples, totems};
        width = getLongest(strings) + 6;
        height = 80;
        for (int i = 0; i < strings.length; i++) {
            Bloomware.Font.drawString(stack, strings[i], x + 3, y + 16 * i + 2, ColorUtils.getHudColor());
        }
    }

    public static int getLongest(String... strings) {
        List<Integer> list = new ArrayList<>();
        for (String s : strings) list.add((int) Bloomware.Font.getStringWidth(s));
        return Collections.max(list);
    }
}
