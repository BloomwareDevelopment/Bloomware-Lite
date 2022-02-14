package me.offeex.bloomware.client.gui.screen.frame.component;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.apache.commons.codec.binary.Base64;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class FontRenderer {
    private final float fontSize;
    private final int startChar;
    private final int endChar;
    private final float[] xPos;
    private final float[] yPos;
    private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]"), patternUnsupported = Pattern.compile("(?i)\\u00A7[L-O]");
    boolean isBig = false;
    private Font font;
    private Graphics2D graphics;
    private FontMetrics metrics;
    private BufferedImage bufferedImage;
    private Identifier resourceLocation;
    private final Color whiteLivesMatter = new Color(255, 255, 255, 0);

    public FontRenderer(Object font, float size) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        setupGraphics2D();
        createFont(font, size);
    }

    private static NativeImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            return NativeImage.read(bais);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public void setBig(boolean big) {
        isBig = big;
    }

    private void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        this.graphics = ((Graphics2D) this.bufferedImage.getGraphics());
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void createFont(Object font, float size) {
        try {
            if ((font instanceof Font))
                this.font = ((Font) font);
            else if ((font instanceof File))
                this.font = Font.createFont(0, (File) font).deriveFont(size);
            else if ((font instanceof InputStream))
                this.font = Font.createFont(0, (InputStream) font).deriveFont(size);
            else {
                this.font = new Font("Verdana", Font.PLAIN, Math.round(size));
            }
            this.graphics.setFont(this.font);
        } catch (Exception e) {
            e.printStackTrace();
            this.font = new Font("Verdana", Font.PLAIN, Math.round(size));
            this.graphics.setFont(this.font);
        }
        this.graphics.setColor(whiteLivesMatter);
        this.graphics.fillRect(0, 0, 256, 256);
        this.graphics.setColor(Color.WHITE);
        this.metrics = this.graphics.getFontMetrics();

        float x = 5.0F;
        float y = 5.0F;
        for (int i = this.startChar; i < this.endChar; i++) {
            this.graphics.drawString(Character.toString((char) i), x, y + this.metrics.getAscent());
            this.xPos[(i - this.startChar)] = x;
            this.yPos[(i - this.startChar)] = (y - this.metrics.getMaxDescent());
            x += this.metrics.stringWidth(Character.toString((char) i)) + 2.0F;
            if (x >= 250 - this.metrics.getMaxAdvance()) {
                x = 5.0F;
                y += this.metrics.getMaxAscent() + this.metrics.getMaxDescent() + this.fontSize / 2.0F;
            }
        }
        String base64 = imageToBase64String(bufferedImage);
        this.setResourceLocation(base64, size);
    }

    private String imageToBase64String(BufferedImage image) {
        String ret;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bos);
            byte[] bytes = bos.toByteArray();
            Base64 encoder = new Base64();
            ret = encoder.encodeAsString(bytes);
            ret = ret.replace(System.lineSeparator(), "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return ret;
    }

    public void setResourceLocation(String base64, float size) {
        NativeImage image = readTexture(base64);
        int imageWidth = Objects.requireNonNull(image).getWidth();
        int imageHeight = image.getHeight();

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                imgNew.setColor(x, y, image.getColor(x, y));
            }
        }

        image.close();
        this.resourceLocation = new Identifier("atomic", "font" + getFont().getFontName().toLowerCase().replace(" ", "-").hashCode() + size);
        applyTexture(resourceLocation, imgNew);
    }

    private void applyTexture(Identifier identifier, NativeImage nativeImage) {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new NativeImageBackedTexture(nativeImage)));
    }

    public final void drawString(MatrixStack matrixStack, String text, float x, float y, FontType fontType, int color, int color2) {
        matrixStack.push();
        //matrixStack.translate(0, 0, 0f);
        text = stripUnsupported(text);

        //Render2DHelper.INSTANCE.setup2DRender(false);
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        String text2 = stripControlCodes(text);
        switch (fontType.ordinal()) {
            case 1:
                drawer(matrixStack, text2, x + 0.5F, y, color2);
                drawer(matrixStack, text2, x - 0.5F, y, color2);
                drawer(matrixStack, text2, x, y + 0.5F, color2);
                drawer(matrixStack, text2, x, y - 0.5F, color2);
                break;
            case 2:
                drawer(matrixStack, text2, x + 0.5F, y + 0.5F, color2);
                break;
            case 3:
                drawer(matrixStack, text2, x + 0.5F, y + 1.0F, color2);
                break;
            case 4:
                drawer(matrixStack, text2, x, y + 0.5F, color2);
                break;
            case 5:
                drawer(matrixStack, text2, x, y - 0.5F, color2);
                break;
            case 6:
                break;
        }

        drawer(matrixStack, text, x, y, color);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        matrixStack.pop();
    }

    public final void drawString(MatrixStack matrixStack, String text, float x, float y, FontType fontType, ColorMutable color) {
        if (color.getRed() < 50 && color.getGreen() < 50 && color.getBlue() < 50) fontType = FontType.NORMAL;
        if (!isBig) matrixStack.scale(0.5f, 0.5f, 1);
        drawString(matrixStack, text, x, y, fontType, color.getRGB(), ColorUtils.modify(color, 0, 0, 0, Math.min(0xBB, color.getAlpha())));
        if (!isBig) matrixStack.scale(2f, 2f, 1);
    }

    private void drawer(MatrixStack matrixStack, String text, float x, float y, int color) {
        StringBuilder finalText = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= this.startChar && c <= this.endChar) finalText.append(c);
            else finalText.append("?");
        }
        text = finalText.toString();
        x *= 2.0F;
        y *= 2.0F;
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.setShaderTexture(0, this.resourceLocation);

        if ((color & -67108864) == 0) {
            color |= -16777216;
        }

        int newColor = color;
        //GL14.glBlendEquation();
        RenderSystem.blendEquation(GL14.GL_FUNC_ADD);
        float startX = x;
        boolean scramble = false;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        for (int i = 0; i < text.length(); i++)
            if ((text.charAt(i) == '\247') && (i + 1 < text.length())) {
                char oneMore = Character.toLowerCase(text.charAt(i + 1));
                if (oneMore == 'n') {
                    y += this.metrics.getAscent() + 2;
                    x = startX;
                } else if (oneMore == 'k') {
                    scramble = true;
                } else if (oneMore == 'r')
                    newColor = color;
                else {
                    newColor = getColorFromCode(oneMore);
                }
                i++;
            } else {
                try {
                    String obfText = "\\:><&%$@!/?";
                    char c = scramble ? obfText.charAt((int) (new Random().nextFloat() * (obfText.length() - 1))) : text.charAt(i);
                    drawChar(matrixStack, c, x, y, newColor);
                    x += getStringWidth(Character.toString(c)) * 2.0F;
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
            }
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public final float getStringWidth(String text) {
        return (float) (getBounds(text).getWidth()) / 2.0F;
    }

    public final float getStringHeight(String text) {
        return (float) getBounds(text).getHeight() / 2.0F;
    }

    private Rectangle2D getBounds(String text) {
        return this.metrics.getStringBounds(text, this.graphics);
    }

    private void drawChar(MatrixStack matrixStack, char character, float x, float y, int color) throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = this.metrics.getStringBounds(Character.toString(character), this.graphics);
        drawTexturedModalRect(matrixStack, x, y, this.xPos[(character - this.startChar)], this.yPos[(character - this.startChar)], (float) bounds.getWidth(), (float) bounds.getHeight() + this.metrics.getMaxDescent() + 1.0F, color);
    }

    private List<String> listFormattedStringToWidth(String s, int width) {
        return Arrays.asList(wrapFormattedStringToWidth(s, width).split("\n"));
    }

    private String wrapFormattedStringToWidth(String s, float width) {
        int wrapWidth = sizeStringToWidth(s, width);

        if (s.length() <= wrapWidth) {
            return s;
        }
        String split = s.substring(0, wrapWidth);
        String split2 = getFormatFromString(split)
                + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ') || (s.charAt(wrapWidth) == '\n') ? 1 : 0));
        try {
            return split + "\n" + wrapFormattedStringToWidth(split2, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private int sizeStringToWidth(String par1Str, float par2) {
        int var3 = par1Str.length();
        float var4 = 0.0F;
        int var5 = 0;
        int var6 = -1;

        for (boolean var7 = false; var5 < var3; var5++) {
            char var8 = par1Str.charAt(var5);

            switch (var8) {
                case '\n':
                    var5--;
                    break;
                case '\247':
                    if (var5 < var3 - 1) {
                        var5++;
                        char var9 = par1Str.charAt(var5);

                        if ((var9 != 'l') && (var9 != 'L')) {
                            if ((var9 == 'r') || (var9 == 'R') || (isFormatColor(var9)))
                                var7 = false;
                        } else
                            var7 = true;
                    }
                    break;
                case ' ':
                case '-':
                case '_':
                case ':':
                    var6 = var5;
                default:
                    String text = String.valueOf(var8);
                    var4 += getStringWidth(text);

                    if (var7) {
                        var4 += 1.0F;
                    }
                    break;
            }
            if (var8 == '\n') {
                var5++;
                var6 = var5;
            } else {
                if (var4 > par2) {
                    break;
                }
            }
        }
        return (var5 != var3) && (var6 != -1) && (var6 < var5) ? var6 : var5;
    }

    private String getFormatFromString(String par0Str) {
        StringBuilder var1 = new StringBuilder();
        int var2 = -1;
        int var3 = par0Str.length();

        while ((var2 = par0Str.indexOf('\247', var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                char var4 = par0Str.charAt(var2 + 1);

                if (isFormatColor(var4))
                    var1 = new StringBuilder("\247" + var4);
                else if (isFormatSpecial(var4)) {
                    var1.append("\247").append(var4);
                }
            }
        }

        return var1.toString();
    }

    private boolean isFormatColor(char par0) {
        return ((par0 >= '0') && (par0 <= '9')) || ((par0 >= 'a') && (par0 <= 'f')) || ((par0 >= 'A') && (par0 <= 'F'));
    }

    private boolean isFormatSpecial(char par0) {
        return ((par0 >= 'k') && (par0 <= 'o')) || ((par0 >= 'K') && (par0 <= 'O')) || (par0 == 'r') || (par0 == 'R');
    }

    private void drawTexturedModalRect(MatrixStack matrixStack, float x, float y, float u, float v, float width, float height, int color) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float scale = 0.0039063F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + height, 0.0f).texture((u + 0.0F) * scale, (v + height) * scale).color(f1, f2, f3, f).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0.0f).texture((u + width) * scale, (v + height) * scale).color(f1, f2, f3, f).next();
        bufferBuilder.vertex(matrix4f, x + width, y + 0.0F, 0.0f).texture((u + width) * scale, (v + 0.0F) * scale).color(f1, f2, f3, f).next();
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + 0.0F, 0.0f).texture((u + 0.0F) * scale, (v + 0.0F) * scale).color(f1, f2, f3, f).next();
    }

    public final String stripControlCodes(String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }

    public final String stripUnsupported(String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }

    public final Graphics2D getGraphics() {
        return this.graphics;
    }

    public final Font getFont() {
        return font;
    }

    private int getColorFromCode(char code) {
        return switch (code) {
            case '0' -> Color.BLACK.getRGB();
            case '1' -> 0xff0000AA;
            case '2' -> 0xff00AA00;
            case '3' -> 0xff00AAAA;
            case '4' -> 0xffAA0000;
            case '5' -> 0xffAA00AA;
            case '6' -> 0xffFFAA00;
            case '7' -> 0xffAAAAAA;
            case '8' -> 0xff555555;
            case '9' -> 0xff5555FF;
            case 'a' -> 0xff55FF55;
            case 'b' -> 0xff55FFFF;
            case 'c' -> 0xffFF5555;
            case 'd' -> 0xffFF55FF;
            case 'e' -> 0xffFFFF55;
            case 'f' -> 0xffffffff;
            case 'g' -> 0xffDDD605;
            default -> -1;
        };
    }

    public void drawString(MatrixStack ms, String title, double x, double y, ColorMutable color) {
        drawString(ms, title, (float) x, (float) y - (getStringHeight(title) - 7) / 2f, FontType.SHADOW_THIN, color);
    }

    public enum FontType {
        NORMAL, SHADOW_THICK, SHADOW_THIN, OUTLINE_THIN, EMBOSS_TOP, EMBOSS_BOTTOM
    }

}
