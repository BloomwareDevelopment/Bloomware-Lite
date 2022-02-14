package me.offeex.bloomware.client.gui.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.frame.Frame;
import me.offeex.bloomware.client.gui.screen.frame.component.components.Element;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ToolBar;
import me.offeex.bloomware.client.gui.screen.frame.component.components.toolbar.ScreenSwitchButton;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.client.Gui;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import static me.offeex.bloomware.Bloomware.mc;

import java.awt.*;
import java.util.ArrayList;

public class HudEditor extends Screen {
    private final ArrayList<Element> elements = new ArrayList<>();
    private final Frame frame;
    private final ToolBar toolBar;

    public HudEditor() {
        super(new LiteralText("Bloomware Gui"));
        int frameX = 10;
        frame = new Frame(Module.Category.HUD);
        frame.setX(frameX);
        toolBar = new ToolBar(0, 0, 0, 0);
        toolBar.addComponent(new ScreenSwitchButton(25, 25, 25, getIcon("elements/gui/toolbar/hudeditor.png"), Bloomware.moduleManager.getModule("HUD")));
        toolBar.addComponent(new ScreenSwitchButton(25, 25, 50, getIcon("elements/gui/toolbar/clickgui.png"), Bloomware.moduleManager.getModule("ClickGUI")));
        toolBar.addComponent(new ScreenSwitchButton(25, 25, 75, getIcon("elements/gui/toolbar/accounts.png"), Bloomware.moduleManager.getModule("Accounts")));
        Bloomware.moduleManager.getModulesByCategory(Module.Category.HUD).forEach(module -> elements.add(new Element(module)));
    }

    @Override
    protected void init() {
        super.init();
        frame.getButtons().forEach(m -> ((ModuleButton)m).setPressed(false));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public Frame getFrame() {
        return frame;
    }

    public static Identifier getIcon(String path) {
        return new Identifier("bloomware", path);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {

        Gui module = Bloomware.moduleManager.getModule(Gui.class);
        if (module.particles.isToggled()) Bloomware.gui.particleManager.render(matrices);
        DrawableHelper.fill(matrices, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0, 0, 0, 100).getRGB());
        frame.renderFrame(matrices);
        frame.updatePosition(mouseX, mouseY);
        frame.getButtons().forEach(c -> c.updateComponent(mouseX, mouseY));
        elements.forEach(element -> {
            if (element.getModule().isEnabled()) {
                element.draw(matrices, mouseX, mouseY, partialTicks);
                element.updatePosition(mouseX, mouseY);
            }
        });
        toolBar.updateProperties(0, mc.getWindow().getScaledHeight() - 25, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        toolBar.render(matrices);
        toolBar.getComponents().forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
    }

    public Element getElementByModule(Module module) {
        return elements.stream().filter(e -> e.getModule().equals(module)).findFirst().orElse(null);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (frame.isHovered(mouseX, mouseY) && mouseButton == 0) {
            frame.setDrag(true);
            frame.dragX = (int) (mouseX - frame.getX());
            frame.dragY = (int) (mouseY - frame.getY());
        }

        elements.forEach(element -> {
            if (element.isHover(mouseX, mouseY) && mouseButton == 0 && element.getModule().isEnabled()) {
                element.setDrag(true);
                element.dragX = (int) (mouseX - element.getX());
                element.dragY = (int) (mouseY - element.getY());
            }
        });
        if (frame.isHovered(mouseX, mouseY) && mouseButton == 1) frame.setOpen(!frame.isOpen());
        if (frame.isOpen() && !frame.getButtons().isEmpty()) frame.getButtons().forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        toolBar.getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        elements.forEach(element -> element.setDrag(false));
        frame.setDrag(false);
        frame.getButtons().stream().filter(b -> frame.isOpen() && !frame.getButtons().isEmpty()).forEach(b -> b.mouseReleased(mouseX, mouseY, mouseButton));
        toolBar.getComponents().forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        frame.getButtons().stream().filter(b -> frame.isOpen() && keyCode != 1 && !frame.getButtons().isEmpty()).forEach(b -> b.keyTyped(keyCode));
        toolBar.getComponents().forEach(component -> component.keyTyped(keyCode));
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) mc.setScreen(null);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
