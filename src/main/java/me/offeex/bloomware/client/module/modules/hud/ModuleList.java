package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Module.Register(name = "ModuleList", description = "Shows enabled modules", category = Module.Category.HUD)
public class ModuleList extends Module {
    private final SettingEnum sortMode = new SettingEnum.Builder("SortMode").modes("Length", "Alphabet").selected("Length").setup(this);
    private final SettingEnum renderMode = new SettingEnum.Builder("RenderMode").modes("Caps", "Small", "Default").selected("Default").setup(this);

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        ArrayList<String> modules = getActiveModules();

        this.width = (int) Bloomware.Font.getStringWidth(getLongest(modules)) + 4;
        this.height = modules.size() * 10 + 4;

        if (sortMode.is("Alphabet")) {
            modules.sort(String.CASE_INSENSITIVE_ORDER);
        } else {
            for (int i = 0; i < modules.size() - 1; i++) {
                for (int j = i + 1; j < modules.size(); j++) {
                    if (Bloomware.Font.getStringWidth(modules.get(i)) > Bloomware.Font.getStringWidth(modules.get(j))) {
                        String temp = modules.get(i);
                        modules.set(i, modules.get(j));
                        modules.set(j, temp);
                    }
                }
            }
            if (y <= mc.getWindow().getScaledHeight() / 2 - this.width)
                Collections.reverse(modules);
        }

        int offset = 0;
        for (String module : modules) {
            switch (renderMode.getSelectedStr()) {
                case "Caps" -> Bloomware.Font.drawString(stack, module.toUpperCase(), shouldOffset() ? x + width - Bloomware.Font.getStringWidth(module.toUpperCase()) : x + 2, y + offset + 2, ColorUtils.getHudColor());
                case "Small" -> Bloomware.Font.drawString(stack, module.toLowerCase(), shouldOffset() ? x + width - Bloomware.Font.getStringWidth(module.toUpperCase()) : x + 2, y + offset + 2, ColorUtils.getHudColor());
                default -> Bloomware.Font.drawString(stack, module, shouldOffset() ? x + width - Bloomware.Font.getStringWidth(module) : x + 2, y + offset + 2, ColorUtils.getHudColor());
            }
            offset += 10;
        }
    }

    private ArrayList<String> getActiveModules() {
        ArrayList<String> modules = new ArrayList<>();
        Bloomware.moduleManager.getModules().forEach(module -> {
            if (module.isEnabled()) modules.add(module.getName());
        });
        return modules;
    }

    private boolean shouldOffset() {
        return x >= mc.getWindow().getScaledWidth() / 2 - this.width;
    }

    private String getLongest(ArrayList<String> modules) {
        return modules.stream().max(Comparator.comparing(mod -> Bloomware.Font.getStringWidth(mod))).orElse("");
    }
}
