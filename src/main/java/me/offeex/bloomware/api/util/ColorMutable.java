package me.offeex.bloomware.api.util;

import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ColorMutable {
    private int value;
    private ArrayList<Runnable> listeners = new ArrayList<>();

    public static final ColorMutable WHITE = new ColorMutable(255, 255, 255);
    public static final ColorMutable BLACK = new ColorMutable(0, 0, 0);
    public static final ColorMutable EMPTY = new ColorMutable(0, 0, 0, 0);
    public static final ColorMutable YELLOW = new ColorMutable(255, 230, 0, 255);
    public static final ColorMutable GREEN = new ColorMutable(0, 255, 0, 255);
    public static final ColorMutable RED = new ColorMutable(255, 0, 0, 255);
    public static final ColorMutable DARK_GRAY = new ColorMutable(26, 26, 26, 255);

    public ColorMutable(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public ColorMutable(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    public ColorMutable(int rgb) {
        value = 0xff000000 | rgb;
    }

    public ColorMutable(String hex) {
        int i = Integer.decode(hex);
        value = (0xFF << 24) | (((i >> 16) & 0xFF) << 16) | (((i >> 8) & 0xFF) << 8) | (i & 0xFF);
    }

    public int getRed() {
        return (getRGB() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 0xFF;
    }

    public int getBlue() {
        return (getRGB()) & 0xFF;
    }

    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    public int getRGB() {
        return value;
    }

    public void setColor(int value) {
        this.value = value;
        listeners.forEach(Runnable::run);
    }

    public Vec3d getVec3d() {
        return new Vec3d(getRed() / 255.0, getGreen() / 255.0, getBlue() / 255.0);
    }

    public void setColorSilent(int value) {
        this.value = value;
    }

    public void setAlpha(float alpha) {
        value = value & 0x00FFFFFF | ((int) (Math.min(255, Math.max(4, alpha * 255))) & 0xFF) << 24;
        listeners.forEach(Runnable::run);
    }

    public void setAlphaSilent(float alpha) {
        value = value & 0x00FFFFFF | ((int) (Math.min(255, Math.max(4, alpha * 255))) & 0xFF) << 24;
    }

    public void setColor(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
        listeners.forEach(Runnable::run);
    }

    public boolean equals(Object obj) {
        return obj instanceof ColorMutable && ((ColorMutable) obj).getRGB() == this.getRGB();
    }

    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
    }

    public static ColorMutable getColor(String nm) {
        return getColor(nm, null);
    }

    public static ColorMutable getColor(String nm, ColorMutable v) {
        Integer intval = Integer.getInteger(nm);
        if (intval == null) {
            return v;
        }
        int i = intval;
        return new ColorMutable((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    public static ColorMutable getColor(String nm, int v) {
        Integer intval = Integer.getInteger(nm);
        int i = (intval != null) ? intval : v;
        return new ColorMutable((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    public static int HSBtoRGB(float[] hsb) {
        return HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 1 -> {
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 2 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                }
                case 3 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 4 -> {
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 5 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                }
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    public static float[] RGBtoHSB(ColorMutable color, float[] hsbvals) {
        return RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
    }

    public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
        float hue, saturation, brightness;
        if (hsbvals == null) {
            hsbvals = new float[3];
        }
        int cmax = Math.max(r, g);
        if (b > cmax) cmax = b;
        int cmin = Math.min(r, g);
        if (b < cmin) cmin = b;

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0)
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            float red = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float green = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float blue = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax)
                hue = blue - green;
            else if (g == cmax)
                hue = 2.0f + red - blue;
            else
                hue = 4.0f + green - red;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public String toHexString() {
        return Integer.toHexString(value);
    }

    public static ColorMutable getHSBColor(float h, float s, float b) {
        return new ColorMutable(HSBtoRGB(h, s, b));
    }

    public static ColorMutable random(Random random) {
        return new ColorMutable(random.nextInt(0, 255), random.nextInt(0, 255), random.nextInt(0, 255));
    }

    public int getTransparency() {
        int alpha = getAlpha();
        if (alpha == 0xff) {
            return Transparency.OPAQUE;
        } else if (alpha == 0) {
            return Transparency.BITMASK;
        } else {
            return Transparency.TRANSLUCENT;
        }
    }

    public static int transparent(int color) {
        return (color & 0x00ffffff) | (1 << 24);
    }

    public void onUpdate(Runnable listener) {
        listeners.add(listener);
    }
}

