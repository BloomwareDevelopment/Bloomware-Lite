package me.offeex.bloomware.client.gui.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.gui.IDraggable;
import me.offeex.bloomware.client.gui.screen.particle.ParticleManager;
import me.offeex.bloomware.client.gui.screen.frame.Frame;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.client.Gui;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static me.offeex.bloomware.Bloomware.mc;

public class ClickGUI extends Screen {
    public static IDraggable dragging = null;
    public TextFieldWidget searchField;
    private static List<Frame> frames;
    protected final ParticleManager particleManager;

    public ClickGUI() {
        super(new LiteralText("Bloomware Gui"));
        frames = new ArrayList<>();
        int frameX = 8;
        int frameY = 12;

        particleManager = new ParticleManager();
        searchField = new TextFieldWidget(mc.textRenderer, 35 + (int) Bloomware.Font.getStringWidth(Bloomware.NAME), mc.getWindow().getScaledHeight() - 20,  150, 17, Text.of(""));
        searchField.setSuggestion("Search...");
        searchField.setMaxLength(30);
        addDrawableChild(searchField);
        /*
            Описание алгоритма сортировки окон
            getStrCount() - рассчитывает количество "этажей" для фреймов.
            calculateMaxElements() - рассчитывает максимальное количество фреймов на одной строке, чтобы они не вылезали за экран.
        */

        for (int i = 0; i < (getStrCount() + 1) * 2; i++) {      // Начинаем итерироваться по "этажам" ((getStrCount() + 1) * 2 не трогать, так надо)
            for (int j = 0; j < calculateMaxElements(); j++) {      // Итерируемся по каждому этажу
                int index = j + (i * calculateMaxElements());     // Получаем индекс относительно "матрицы"
                if (Module.Category.values().length > index && Module.Category.values()[index] != Module.Category.HUD) {     // Избегаем лишних итераций
                    Frame frame = new Frame(Module.Category.values()[index]);     // Создаем фрейм с определенным индексом
                    frames.add(frame);     // Добавляем его в аррейлист
                    /*
                        Установка координаты X
                        Смотрим, есть ли этажи выше или нет (i == 0)
                        Если нет, то мы начинаем выставлять оффсет (frameX + ((frame.getWidth() + 12) * j))
                        Если есть, то мы берем X с фрейма, который находится над нашим.
                    */
                    frames.get(index).setX(i == 0 ? frameX + (frameX + frame.getWidth()) * j : frames.get(j).getX());
                    /*
                        Установка координаты Y
                        Смотрим, есть ли этажи выше или нет (i == 0)
                        Если нет, то мы ставим дефолтный Y (frameY)
                        Если есть, то нам надо найти высоту, и координату Y фрейма, который находится над нашим
                        Это можно сделать с помощью формулы (frames.get(index - calculateMaxElements()))
                    */
                    int shouldOffsetY = i == 0 ? frameY : frames.get(index - calculateMaxElements()).getY() + frames.get(index - calculateMaxElements()).getHeight();
                    frames.get(index).setY(shouldOffsetY + 3);
                }
            }
        }
    }

    private short calculateMaxElements() {
        return (short) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / (24 + 110) / 2);
    }

    private int getStrCount() {
        return Module.Category.values().length / calculateMaxElements() + Module.Category.values().length % calculateMaxElements() == 0 ? 0 : 1;
    }

    @Override
    public void init() {
        frames.forEach(f -> f.getButtons().forEach(m -> ((ModuleButton) m).setPressed(false)));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public Frame getFrameByCategory(String category) {
        return frames.stream().filter(frame -> frame.category.getName().equalsIgnoreCase(category)).findAny().orElse(null);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
        Gui module = Bloomware.moduleManager.getModule(Gui.class);
        if (module.particles.isToggled()) particleManager.render(matrices);
        DrawableHelper.fill(matrices, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new ColorMutable(0, 0, 0, 100).getRGB());

        if (module.hueDown.isToggled())
            fillGradient(matrices, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 0, 0, module.downColor.getColor().getRGB(), ColorMutable.EMPTY.getRGB());
        if (module.hueUp.isToggled())
            fillGradient(matrices, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), module.upColor.getColor().getRGB(), ColorMutable.EMPTY.getRGB());
        frames.forEach(frame -> {
            frame.renderFrame(matrices);
            frame.updatePosition(mouseX, mouseY);
            frame.getButtons().forEach(c -> c.updateComponent(mouseX, mouseY));
        });
        Bloomware.hud.getToolBar().updateProperties(0, mc.getWindow().getScaledHeight() - 25, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        Bloomware.hud.getToolBar().render(matrices);
        Bloomware.hud.getToolBar().getComponents().forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
        searchField.x = 35 + (int) Bloomware.Font.getStringWidth(Bloomware.NAME);
        searchField.y = mc.getWindow().getScaledHeight() - 20;
        searchField.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            if (frame.isHovered(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frames.remove(i);
                frames.add(frame);
                frame.dragX = (int) (mouseX - frame.getX());
                frame.dragY = (int) (mouseY - frame.getY());
            }
            if (frame.isHovered(mouseX, mouseY) && mouseButton == 1) frame.setOpen(!frame.isOpen());
            if (frame.isOpen() && !frame.getButtons().isEmpty())
                frame.getButtons().forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        }
        Bloomware.hud.getToolBar().getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        searchField.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            frame.setDrag(false);
        }
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            frame.getButtons().stream().filter(b -> frame.isOpen() && !frame.getButtons().isEmpty()).forEach(b -> b.mouseReleased(mouseX, mouseY, mouseButton));
        }
        Bloomware.hud.getToolBar().getComponents().forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        searchField.mouseReleased(mouseX, mouseY, mouseButton);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            frame.getButtons().stream().filter(b -> frame.isOpen() && keyCode != 1 && !frame.getButtons().isEmpty()).forEach(button -> button.keyTyped(keyCode));
        }
        Bloomware.hud.getToolBar().getComponents().forEach(c -> c.keyTyped(keyCode));
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) mc.setScreen(null);
        searchField.keyPressed(keyCode, scanCode, modifiers);
        searchField.setSuggestion(searchField.getText().equals("") ? "Search..." : "");
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean charTyped(char c, int i) {
        searchField.charTyped(c, i);
        searchField.setSuggestion(searchField.getText().equals("") ? "Search..." : "");
        return super.charTyped(c, i);
    }

    public void fillGradient(MatrixStack matrix, int x, int y, int x1, int y1, int color, int color1) {
        super.fillGradient(matrix, x, y, x1, y1, color, color1);
    }
}
