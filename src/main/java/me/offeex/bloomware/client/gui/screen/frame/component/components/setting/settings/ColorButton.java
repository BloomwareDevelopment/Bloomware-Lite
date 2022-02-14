package me.offeex.bloomware.client.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.client.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class ColorButton extends SettingButton {
    private static final float HEIGHT = Bloomware.Font.getStringHeight("#");
    private boolean open;
    private ChangingProperty current = ChangingProperty.NONE;
    private final ColorMutable color = ((SettingColor) setting).getColor();
    private final float[] hsba = new float[4];

    public ColorButton(SettingColor settingColor, ModuleButton button, int offsetY) {
        super(settingColor, button, offsetY);
        float[] hsb = ColorMutable.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsba[0] = hsb[0];
        hsba[1] = hsb[1];
        hsba[2] = hsb[2];
        hsba[3] = color.getAlpha() / 255f;
        color.onUpdate(() -> {
            float[] hsb1 = ColorMutable.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            hsba[0] = hsb1[0];
            hsba[1] = hsb1[1];
            hsba[2] = hsb1[2];
            hsba[3] = color.getAlpha() / 255f;
        });
    }

    @Override
    public int getHeight() {
        return (open ? 110 : 0) + COMPONENT_HEIGHT;
    }

    @Override
    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + getHeight();
    }

    @Override
    public void setOffsetY(final int offsetY) {
        this.offsetY = offsetY;
    }

    enum ChangingProperty {
        NONE, SQUARE, HUE, ALPHA, COPY, PASTE
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (!isHovered(mouseX, mouseY)) return;
        if (button == 0 && open && this.button.isOpen()) {
            if (isHovered && current == ChangingProperty.NONE) {
                if (mouseX > x + 5 && mouseX < x + 5 + 75 && mouseY > y + COMPONENT_HEIGHT + 5 && mouseY < y + COMPONENT_HEIGHT + 5 + 75) {
                    current = ChangingProperty.SQUARE;
                } else if (mouseX > x + 80 + 5 && mouseX < x + 80 + 5 + 8 && mouseY > y + COMPONENT_HEIGHT + 5 && mouseY < y + COMPONENT_HEIGHT + 5 + 75) {
                    current = ChangingProperty.HUE;
                } else if (mouseX > x + 80 + 5 + 5 + 8 && mouseX < x + 80 + 5 + 5 + 8 + 8 && mouseY > y + COMPONENT_HEIGHT + 5 && mouseY < y + COMPONENT_HEIGHT + 5 + 75) {
                    current = ChangingProperty.ALPHA;
                } else if (mouseX > x + 5 && mouseX < x + 43 && mouseY > y + COMPONENT_HEIGHT + HEIGHT + 80 && mouseY < y + COMPONENT_HEIGHT + HEIGHT * 2 + 80) {
                    current = ChangingProperty.COPY;
                } else if (mouseX > x + 43 && mouseX < x + 80 && mouseY > y + COMPONENT_HEIGHT + HEIGHT + 80 && mouseY < y + COMPONENT_HEIGHT + HEIGHT * 2 + 80) {
                    current = ChangingProperty.PASTE;
                } else {
                    current = ChangingProperty.NONE;
                }
            }
        }
        if (button == 1) {
            open = !open;
            this.button.frame.update();
            current = ChangingProperty.NONE;
        }
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (open && this.button.isOpen()) current = ChangingProperty.NONE;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        super.updateComponent(mouseX, mouseY);
        switch (current) {
            case HUE -> {
                float value = (float) Math.max(0f, Math.min(1f, (mouseY - (y + COMPONENT_HEIGHT + 5)) / 75f));
                hsba[0] = value;
                color.setColorSilent(ColorMutable.HSBtoRGB(hsba[0], hsba[1], hsba[2]) & 0x00FFFFFF | (int) (hsba[3] * 255) << 24);
            }
            case ALPHA -> {
                float value = 1f - (float) Math.max(0f, Math.min(1f, (mouseY - (y + COMPONENT_HEIGHT + 5)) / 75f));
                hsba[3] = value;
                color.setAlphaSilent(value);
            }
            case SQUARE -> {
                float valueX = (float) Math.max(0f, Math.min(1f, (mouseX - (x + 5)) / 75f));
                float valueY = 1f - (float) Math.max(0f, Math.min(1f, (mouseY - (y + COMPONENT_HEIGHT + 5)) / 75f));
                hsba[1] = valueX;
                hsba[2] = valueY;
                color.setColorSilent(ColorMutable.HSBtoRGB(hsba[0], hsba[1], hsba[2]) & 0x00FFFFFF | (int) (hsba[3] * 255) << 24);
            }
            case COPY -> {
                mc.keyboard.setClipboard(color.toHexString());
                current = ChangingProperty.NONE;
            }
            case PASTE -> {
                color.setColor((int) Long.parseLong(mc.keyboard.getClipboard(), 16));
                current = ChangingProperty.NONE;
            }
        }
    }

    public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix,
                button.frame.getX(), button.frame.getY() + offsetY,
                button.frame.getX() + button.frame.getWidth(), button.frame.getY() + offsetY + COMPONENT_HEIGHT,
                isHovered ? 0x96000000 : 0x82000000);
        Bloomware.Font.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY, ColorMutable.WHITE);

        DrawableHelper.fill(matrix,
                button.frame.getX() + button.frame.getWidth() - 17, button.frame.getY() + offsetY + 3,
                button.frame.getX() + button.frame.getWidth() - 2, button.frame.getY() + offsetY + COMPONENT_HEIGHT - 3, color.getRGB());

        if (open && this.button.isOpen()) {
            DrawableHelper.fill(matrix, x, y + COMPONENT_HEIGHT, x + button.frame.getWidth(), y + getHeight(), isHovered ? 0x96000000 : 0x82000000);
            if (current == ChangingProperty.SQUARE)
                fill(matrix, x + 4, y + COMPONENT_HEIGHT + 4, 77, 77, 0xffffffff);
            RenderUtil.draw4Gradient(matrix.peek().getPositionMatrix(), x + 5, y + COMPONENT_HEIGHT + 5, 75, 75, 0xFFFFFF, getCurrentHueColor(), 0x000000, 0x000000);
            drawPointer2d(matrix, x + 5, y + COMPONENT_HEIGHT + 5, 75, 75, hsba[1], 1f - hsba[2]);

            if (current == ChangingProperty.HUE)
                fill(matrix, x + 84, y + COMPONENT_HEIGHT + 4, 10, 77, 0xffffffff);
            drawHueSelector(matrix, x + 80 + 5, y + COMPONENT_HEIGHT + 5, 8, 75);
            drawPointerVertical(matrix, x + 80 + 5, y + COMPONENT_HEIGHT + 5, 8, 75, hsba[0]);

            if (current == ChangingProperty.ALPHA)
                fill(matrix, x + 97, y + COMPONENT_HEIGHT + 4, 10, 77, 0xffffffff);
            drawAlphaSelector(matrix, x + 80 + 5 + 5 + 8, y + COMPONENT_HEIGHT + 5, 8, 75);
            drawPointerVertical(matrix, x + 80 + 5 + 5 + 8, y + COMPONENT_HEIGHT + 5, 8, 75, 1f - hsba[3]);

            Bloomware.Font.drawString(matrix, "#" + color.toHexString(), x + 42 - Bloomware.Font.getStringWidth(color.toHexString()) / 2, y + COMPONENT_HEIGHT + 80, ColorMutable.WHITE);
            fill(matrix, x + 5, (int) (y + COMPONENT_HEIGHT + 82 + HEIGHT), 38, (int) HEIGHT, current == ChangingProperty.COPY ? 0x4E000000 : 0x34000000);
            fill(matrix, x + 45, (int) (y + COMPONENT_HEIGHT + 82 + HEIGHT), 37, (int) HEIGHT, current == ChangingProperty.PASTE ? 0x4E000000 : 0x34000000);
            Bloomware.Font.drawString(matrix, "Copy", x + 24 - Bloomware.Font.getStringWidth("Copy") / 2, y + COMPONENT_HEIGHT + 80 + HEIGHT, ColorMutable.WHITE);
            Bloomware.Font.drawString(matrix, "Paste", x + 77 - Bloomware.Font.getStringWidth("Paste"), y + COMPONENT_HEIGHT + 80 + HEIGHT, ColorMutable.WHITE);
        }
    }

    private int getCurrentHueColor() {
        return ColorMutable.HSBtoRGB(hsba[0], 1, 1);
    }

    private void drawHueSelector(MatrixStack matrix, float x, float y, float w, float h) {
        RenderUtil.drawVGradient(matrix.peek().getPositionMatrix(), x, y, w, h, 0xffff0000, 0xffffff00, 0xff00ff00, 0xff00ffff, 0xff0000ff, 0xffff00ff, 0xffff0000);
    }

    private void drawAlphaSelector(MatrixStack matrix, float x, float y, float w, float h) {
        float stepY = w / 2;
        for (int i = 0; i < h / stepY; i++) {
            DrawableHelper.fill(matrix, (int) x, (int) (y + stepY * i), (int) (x + (w / 2)), (int) (y + Math.min(h, stepY * (i + 1))), (i % 2 == 0) ? 0xFFFFFFFF : 0xFF888888);
            DrawableHelper.fill(matrix, (int) (x + (w / 2)), (int) (y + stepY * i), (int) (x + w), (int) (y + Math.min(h, stepY * (i + 1))), (i % 2 == 0) ? 0xFF888888 : 0xFFFFFFFF);
        }
        Bloomware.gui.fillGradient(matrix, (int) x, (int) y, (int) (x + w), (int) (y + h), getCurrentHueColor(), 0);
    }

    private void drawPointerVertical(MatrixStack matrix, float x, float y, float w, float h, float value) {
        DrawableHelper.fill(matrix, (int) (x - 1), (int) (y + (h * value)), (int) (x + w + 1), (int) (y + (h * value) + 1), 0xFFFFFFFF);
    }

    private void drawPointer2d(MatrixStack matrix, float x, float y, float w, float h, float valueX, float valueY) {
        DrawableHelper.fill(matrix, (int) (x + (w * valueX) - 2), (int) (y + (h * valueY) - 2), (int) (x + (w * valueX) + 2), (int) (y + (h * valueY) + 2), 0xFF000000);
        DrawableHelper.fill(matrix, (int) (x + (w * valueX) - 1), (int) (y + (h * valueY) - 1), (int) (x + (w * valueX) + 1), (int) (y + (h * valueY) + 1), 0xFFFFFFFF);
    }

    private void fill(MatrixStack matrix, int x, int y, int w, int h, int color) {
        DrawableHelper.fill(matrix, x, y, x + w, y + h, color);
    }
}
