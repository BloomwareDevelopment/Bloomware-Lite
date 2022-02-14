package me.offeex.bloomware.client.module.modules.screen;

import me.offeex.bloomware.api.util.ClipboardImage;
import me.offeex.bloomware.client.module.Module;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

@Module.Register(name = "BetterScreenshot", description = "Adds some new features to screenshots", category = Module.Category.CAMERA)
public class BetterScreenshot extends Module {
    public static Image getLatestScreenshot() throws IOException {
        File path = new File(mc.runDirectory.getAbsolutePath() + "/screenshots/");
        Optional<Path> lastFilePath = Files.list(path.toPath())
                .filter(f -> !Files.isDirectory(f))
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()));
        return new ImageIcon(lastFilePath.get().toString()).getImage();
    }

    public static void copyToClipboard(Image image) {
        new Thread(() -> {
            ClipboardImage image1 = new ClipboardImage(image);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(image1, null);
        }).start();
    }
}
