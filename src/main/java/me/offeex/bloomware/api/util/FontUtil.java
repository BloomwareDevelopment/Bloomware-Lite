package me.offeex.bloomware.api.util;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.gui.screen.frame.component.FontRenderer;

public class FontUtil {
    public void generateFonts(String path, int size) {
        Bloomware.Font = new FontRenderer(Bloomware.class.getClassLoader().getResourceAsStream(path), size);
    }
}
