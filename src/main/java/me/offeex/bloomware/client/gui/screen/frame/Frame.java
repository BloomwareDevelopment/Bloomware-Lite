package me.offeex.bloomware.client.gui.screen.frame;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.IDraggable;
import me.offeex.bloomware.client.gui.screen.ClickGUI;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.client.Gui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class Frame implements IDraggable {

    MinecraftClient mc = MinecraftClient.getInstance();

    public Module.Category category;
    private final ArrayList<Component> buttons;
    private boolean open;
    private int x;
    private int y;
    private final int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private final int width;
    private int height;

    public Frame(Module.Category category) {
        this.category = category;
        this.buttons = new ArrayList<>();
        this.width = 110;
        this.x = 5;
        this.y = 5;
        this.barHeight = 16;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int componentY = this.barHeight;
        for (Module m : Bloomware.moduleManager.getModulesByCategory(category)) {
            ModuleButton moduleButton = new ModuleButton(m, this, componentY);
            buttons.add(moduleButton);
            componentY += Component.COMPONENT_HEIGHT;
        }
        update();
    }

    public void renderFrame(MatrixStack matrix) {
        if (isDragging)
            DrawableHelper.fill(matrix, x, y, x + width, y + barHeight, new Color(50, 50, 50, 200).getRGB());
        else
            DrawableHelper.fill(matrix, x, y, x + width, y + barHeight, ColorUtils.getColor("ElementFrame").getRGB());

        switch (Bloomware.moduleManager.getModule(Gui.class).categoryOffset.getSelectedStr()) {
            case "Center" -> Bloomware.Font.drawString(new MatrixStack(), category.getName(), x + width / 2f - Bloomware.Font.getStringWidth(category.getName()) / 2f, y, ColorUtils.getTextColor());
            case "Left" -> Bloomware.Font.drawString(new MatrixStack(), category.getName(), x + 2, y, ColorUtils.getTextColor());
            case "Right" -> Bloomware.Font.drawString(new MatrixStack(), category.getName(), x + width - Bloomware.Font.getStringWidth(category.getName()) - 2, y, ColorUtils.getTextColor());
        }

        if (open && !buttons.isEmpty()) {
            buttons.forEach(c -> {
                if (c instanceof ModuleButton modButton && modButton.module.getName().toLowerCase(Locale.ROOT).contains(Bloomware.gui.searchField.getText().toLowerCase(Locale.ROOT))) c.render(matrix);
            });
            DrawableHelper.fill(matrix, this.x, y + 16, this.x + 1, y + height, ColorUtils.getGuiColor().getRGB());
            DrawableHelper.fill(matrix, this.x, y + height, this.x + width, y + height + 1, ColorUtils.getGuiColor().getRGB());
            DrawableHelper.fill(matrix, this.x, y + barHeight - 1, this.x + width, y + barHeight, ColorUtils.getGuiColor().getRGB());
        }
    }

    public ArrayList<Component> getButtons() {
        return this.buttons;
    }

    public void setX(final int newX) {
        this.x = newX;
    }

    public void setY(final int newY) {
        this.y = newY;
    }

    public void setDrag(final boolean drag) {
        if (drag && ClickGUI.dragging == null) {
            ClickGUI.dragging = this;
            this.isDragging = true;
        }
        else {
            if (ClickGUI.dragging == this)
                ClickGUI.dragging = null;
            this.isDragging = false;
        }
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }

    public void update() {
        int offY = this.barHeight;
        for (final Component comp : this.buttons) {
            comp.setOffsetY(offY);
            offY += comp.getHeight();
        }
        this.height = offY;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return height;
    }

    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(Math.max(0, Math.min(mc.getWindow().getScaledWidth() - this.width, mouseX - dragX)));
            this.setY(Math.max(0, Math.min(mc.getWindow().getScaledHeight() - this.height, mouseY - dragY)));
        }
    }

    public boolean isHovered(final double x, final double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
}
