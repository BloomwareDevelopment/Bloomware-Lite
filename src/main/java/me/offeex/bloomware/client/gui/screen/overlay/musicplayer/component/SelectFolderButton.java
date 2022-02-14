package me.offeex.bloomware.client.gui.screen.overlay.musicplayer.component;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.overlay.Overlay;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import javax.swing.*;
import java.awt.*;

public class SelectFolderButton extends Component {
    private int x;
    private int y;
    private final int width = 100;
    private final int height = 15;

    @Override
    public void render(MatrixStack matrix) {
        this.x = mc.getWindow().getScaledWidth() - 300;
        this.y = 60;
        DrawableHelper.fill(matrix, x, y, x + width, y + height, Overlay.BLACK_OPACITY.getRGB());
        Bloomware.Font.drawString(matrix,"Select folder...", x + 2, y + 1, ColorMutable.WHITE);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            EventQueue.invokeLater(() -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Choose music folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Bloomware.musicManager.loadFolder(chooser.getSelectedFile());
                    Bloomware.overlay.reloadTracks();
                    chooser.cancelSelection();
                    chooser.setVisible(false);
                }
            });
        }
    }

    public boolean isHovered(double x, double y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + height;
    }
}
