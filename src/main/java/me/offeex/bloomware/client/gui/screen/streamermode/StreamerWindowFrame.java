package me.offeex.bloomware.client.gui.screen.streamermode;

import me.offeex.bloomware.Bloomware;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class StreamerWindowFrame extends JFrame {
    private final StreamerWindow window = new StreamerWindow();

    public StreamerWindowFrame() {
        try {
            this.initUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() throws IOException {
        this.add(window);
        this.setResizable(true);
        this.pack();
        this.setTitle("Bloomware - Streamer Mode");
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Bloomware.moduleManager.getModule("StreamerMode").disable();
            }
        });
        Image image = ImageIO.read(Bloomware.class.getResourceAsStream("/assets/bloomware/elements/tray/icon16x16.png"));
        this.setIconImage(image);
    }

    public void setStrings(ArrayList<String> strings) {
        window.setStrings(strings);
    }
}
