package me.offeex.bloomware.client.gui.screen.streamermode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StreamerWindow extends JPanel {
    private ArrayList<String> toDraw = new ArrayList<>();
    private final Font font;
    private final FontMetrics metrics;

    public StreamerWindow() {
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(600, 480));
        font = new Font("Verdana", Font.PLAIN, 20);
        metrics = getFontMetrics(font);
    }

    public void setStrings(ArrayList<String> strings) {
        toDraw = strings;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.render(g);
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.setFont(font);
        short offset = 40;
        for (String s : toDraw) {
            g.drawString(s, (this.getWidth() - metrics.stringWidth(s)) / 2, offset);
            offset += 20;
        }
        Toolkit.getDefaultToolkit().sync();
    }
}
