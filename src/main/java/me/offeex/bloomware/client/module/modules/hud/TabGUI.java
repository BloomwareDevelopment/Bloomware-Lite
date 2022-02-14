package me.offeex.bloomware.client.module.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

@Module.Register(name = "TabGUI", description = "It's tab gui lol", category = Module.Category.HUD)
public class TabGUI extends Module {
    private int selectedCategory = 0, selectedModule = -1;
    private final ColorMutable darkBlack = new ColorMutable(0, 0, 0, 70);

    public TabGUI() {
        this.width = 80;
        this.height = 120;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        short offset = 0;
        for (Category category : Module.Category.values()) {
            DrawableHelper.fill(stack, x, y + offset, x + width, y + offset + 13, Category.values()[selectedCategory] == category ? ColorUtils.getHudColor().getRGB() : darkBlack.getRGB());
            Bloomware.Font.drawString(stack, category.getName(), Category.values()[selectedCategory] == category ? x + 4 : x + 2, y + offset, ColorMutable.WHITE);
            offset += 13;
        }
        offset = 0;
        if (selectedModule != -1) {
            for (Module module : Bloomware.moduleManager.getModulesByCategory(Category.values()[selectedCategory])) {
                DrawableHelper.fill(stack, x + width + 1, y + offset, x + width + 101, y + offset + 13, Bloomware.moduleManager.getModulesByCategory(Module.Category.values()[selectedCategory]).get(selectedModule) == module ? ColorUtils.getHudColor().getRGB() : darkBlack.getRGB());
                Bloomware.Font.drawString(stack, module.getName(), x + width + 3, y + offset, module.isEnabled() ? ColorUtils.getHudColor() : ColorMutable.WHITE);
                offset += 13;
            }
        }
    }

    @Subscribe
    private void onKeyPressed(EventKeyPress event) {
        switch (event.getKey()) {
            case GLFW.GLFW_KEY_DOWN -> {
                if (selectedModule == -1) selectedCategory = Category.values().length - 1 >= selectedCategory + 1 ? selectedCategory + 1 : 0;
                else selectedModule = Bloomware.moduleManager.getModulesByCategory(Module.Category.values()[selectedCategory]).size() - 1 >= selectedModule + 1 ? selectedModule + 1 : 0;
            }
            case GLFW.GLFW_KEY_UP -> {
                if (selectedModule == -1) selectedCategory = selectedCategory == 0 ? Category.values().length - 1 : selectedCategory - 1;
                else selectedModule = selectedModule == 0 ? Bloomware.moduleManager.getModulesByCategory(Module.Category.values()[selectedCategory]).size() - 1 : selectedModule - 1;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                if (selectedModule == -1) selectedModule = 0;
                else Bloomware.moduleManager.getModulesByCategory(Module.Category.values()[selectedCategory]).get(selectedModule).toggle();
            }
            case GLFW.GLFW_KEY_LEFT -> selectedModule = -1;
        }
    }
}
