package me.offeex.bloomware.client.gui.screen.accounteditor.component.components.altmanager;

import me.offeex.bloomware.client.gui.screen.accounteditor.component.PictureWindow;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class AltsWindow extends PictureWindow {
    private final Identifier TEXTURE;
    private final ArrayList<Component> alts;
    private final int x, y, width, height;

    public AltsWindow(ArrayList<Component> components, Identifier texture, int x, int y, int width, int height) {
        super(components, texture, x, y, width, height);
        this.TEXTURE = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alts = components;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);
        int xOffset, yOffset = 12, count = 0;
        for (Component component : alts) {
            xOffset = count % 2 == 0 ? 12 : 131;
            if (count != 0) yOffset += count % 2 == 0 ? 116 : 0;
            ((AltWidget) component).setXOffset(xOffset);
            ((AltWidget) component).setYOffset(yOffset);
            count++;
        }
    }
}
