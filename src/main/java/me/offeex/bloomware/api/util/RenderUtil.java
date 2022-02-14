package me.offeex.bloomware.api.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.modules.client.Colors;
import me.offeex.bloomware.mixins.accessors.IInGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;

import static me.offeex.bloomware.Bloomware.mc;

public class RenderUtil {

    public static void drawFilledBox(MatrixStack stack, Box box, ColorMutable c) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        setup3D();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB()).next();

        tessellator.draw();
        clean3D();
    }

    public static void drawOutline(MatrixStack stack, Box box, ColorMutable c, double lineWidth) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Colors module = Bloomware.moduleManager.getModule(Colors.class);
        double distance = Math.sqrt(mc.player.squaredDistanceTo(box.getCenter()));
        setup3D();
//        TODO: 3D width scaling
        RenderSystem.lineWidth((float) (module.widthMode.is("2D") ? lineWidth : lineWidth * (-0.025f * (distance <= 8 ? 1 : distance * 0.1))));
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);

        if (module.blendMode.is("Add")) RenderSystem.blendFunc(770, 1);
        else RenderSystem.defaultBlendFunc();

        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

        WorldRenderer.drawBox(stack, bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

        tessellator.draw();
        clean3D();
    }

    public static void drawLine(MatrixStack stack, Vec3d startPos, Vec3d endPos, ColorMutable c, double lineWidth) {
        float startX = (float) startPos.x;
        float startY = (float) startPos.y;
        float startZ = (float) startPos.z;
        float endX = (float) (endPos.x - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float endY = (float) (endPos.y - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float endZ = (float) (endPos.z - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float x = endX - startX;
        float y = endY - startY;
        float z = endZ - startZ;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Matrix3f normal = stack.peek().getNormalMatrix();
        Colors module = Bloomware.moduleManager.getModule(Colors.class);
        double distance = Math.sqrt(x * x + y * y + z * z);

        setup3D();
        RenderSystem.lineWidth((float) (module.widthMode.is("2D") ? lineWidth : lineWidth / distance));
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);

        if (module.blendMode.is("Add")) RenderSystem.blendFunc(770, 1);
        else RenderSystem.defaultBlendFunc();

        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

//        TODO: Normal vectors (tracers having different width while rotating)
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), startX, startY, startZ).color(c.getRGB()).normal(normal, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), endX, endY, endZ).color(c.getRGB()).normal(normal, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), startX, startY, startZ).color(c.getRGB()).normal(normal, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), endX, endY, endZ).color(c.getRGB()).normal(normal, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), startX, startY, startZ).color(c.getRGB()).normal(normal, 0.0f, 0.0f, 1.0f).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), endX, endY, endZ).color(c.getRGB()).normal(normal, 0.0f, 0.0f, 1.0f).next();

        tessellator.draw();
        clean3D();
    }

    public static void draw4Gradient(Matrix4f matrix, float x, float y, float w, float h, int topLeft, int topRight, int bottomRight, int bottomLeft) {
        float[] topLeftF = {(topLeft >> 16 & 255) / 255.0F, (topLeft >> 8 & 255) / 255.0F, (topLeft & 255) / 255.0F};
        float[] topRightF = {(topRight >> 16 & 255) / 255.0F, (topRight >> 8 & 255) / 255.0F, (topRight & 255) / 255.0F};
        float[] bottomRightF = {(bottomRight >> 16 & 255) / 255.0F, (bottomRight >> 8 & 255) / 255.0F, (bottomRight & 255) / 255.0F};
        float[] bottomLeftF = {(bottomLeft >> 16 & 255) / 255.0F, (bottomLeft >> 8 & 255) / 255.0F, (bottomLeft & 255) / 255.0F};
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(matrix, x, y + h, 0f).color(bottomLeftF[0], bottomLeftF[1], bottomLeftF[2], 1).next();
        bufferbuilder.vertex(matrix, x + w, y + h, 0f).color(bottomRightF[0], bottomRightF[1], bottomRightF[2], 1).next();
        bufferbuilder.vertex(matrix, x + w, y, 0f).color(topRightF[0], topRightF[1], topRightF[2], 1).next();
        bufferbuilder.vertex(matrix, x, y, 0f).color(topLeftF[0], topLeftF[1], topLeftF[2], 1).next();
        tessellator.draw();
        clean();
    }

    public static void drawHGradient(Matrix4f matrix, float x, float y, float w, float h, int... colors) {
        if (colors.length < 2) return;
        float[][] colorsF = new float[colors.length][4];
        for (int i = 0; i < colors.length; i++) {
            colorsF[i] = new float[]{(colors[i] >> 24 & 255) / 255.0F, (colors[i] >> 16 & 255) / 255.0F, (colors[i] >> 8 & 255) / 255.0F, (colors[i] & 255) / 255.0F};
        }
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        float step = w / colors.length;
        for (int i = 1; i < colors.length; i++) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(matrix, x + (i == (colors.length - 1) ? w : step * i), y, 0f).color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next();
            bufferbuilder.vertex(matrix, x + (step * (i - 1)), y, 0f).color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next();
            bufferbuilder.vertex(matrix, x + (step * (i - 1)), y + h, 0f).color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next();
            bufferbuilder.vertex(matrix, x + (i == (colors.length - 1) ? w : step * i), y + h, 0f).color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next();
            tessellator.draw();
        }
        clean();
    }

    public static void drawVGradient(Matrix4f matrix, float x, float y, float w, float h, int... colors) {
        if (colors.length < 2) return;
        float[][] colorsF = new float[colors.length][4];
        for (int i = 0; i < colors.length; i++) {
            colorsF[i] = new float[]{(colors[i] >> 24 & 255) / 255.0F, (colors[i] >> 16 & 255) / 255.0F, (colors[i] >> 8 & 255) / 255.0F, (colors[i] & 255) / 255.0F};
        }
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        float step = h / colors.length;
        for (int i = 1; i < colors.length; i++) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(matrix, x, y + (i == (colors.length - 1) ? h : step * i), 0f).color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next();
            bufferbuilder.vertex(matrix, x + w, y + (i == (colors.length - 1) ? h : step * i), 0f).color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next();
            bufferbuilder.vertex(matrix, x + w, y + (step * (i - 1)), 0f).color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next();
            bufferbuilder.vertex(matrix, x, y + (step * (i - 1)), 0f).color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next();
            tessellator.draw();
        }
        clean();
    }

    public static void drawPolygonPart(double x, double y, int radius, int part, int startColor, int endColor) {
        float alpha = (float) (startColor >> 24 & 255) / 255.0F;
        float red = (float) (startColor >> 16 & 255) / 255.0F;
        float green = (float) (startColor >> 8 & 255) / 255.0F;
        float blue = (float) (startColor & 255) / 255.0F;
        float alpha1 = (float) (endColor >> 24 & 255) / 255.0F;
        float red1 = (float) (endColor >> 16 & 255) / 255.0F;
        float green1 = (float) (endColor >> 8 & 255) / 255.0F;
        float blue1 = (float) (endColor & 255) / 255.0F;
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(x, y, 0).color(red, green, blue, alpha).next();
        final double TWICE_PI = Math.PI * 2;
        for (int i = part * 90; i <= part * 90 + 90; i++) {
            double angle = (TWICE_PI * i / 360) + Math.toRadians(180);
            bufferbuilder.vertex(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).color(red1, green1, blue1, alpha1).next();
        }
        tessellator.draw();
        clean();
    }

    public static void drawGlow(MatrixStack matrix, double x, double y, double x1, double y1, int color) {
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Bloomware.gui.fillGradient(matrix, (int) x, (int) y, (int) x1, (int) (y + (y1 - y) / 2f), ColorMutable.transparent(color), color);
        Bloomware.gui.fillGradient(matrix, (int) x, (int) (y + (y1 - y) / 2f), (int) x1, (int) y1, color, ColorMutable.transparent(color));
        int radius = (int) ((y1 - y) / 2f);
        drawPolygonPart(x, (y + (y1 - y) / 2f), radius, 0, color, ColorMutable.transparent(color));
        drawPolygonPart(x, (y + (y1 - y) / 2f), radius, 1, color, ColorMutable.transparent(color));
        drawPolygonPart(x1, (y + (y1 - y) / 2f), radius, 2, color, ColorMutable.transparent(color));
        drawPolygonPart(x1, (y + (y1 - y) / 2f), radius, 3, color, ColorMutable.transparent(color));
        clean();
    }

    // ПАСТА БЛИЧХАКА ALERT ЭВАКУАЦИЯ

    public static void drawText(String text, Vec3d pos, double offX, double offY, double scale, double textOffset, ColorMutable color) {
        MatrixStack matrices = matrixFrom(pos);
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.translate(offX, offY, 0);
        matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        Bloomware.Font.drawString(matrices, text, textOffset, 0f, color);
        immediate.draw();
        RenderSystem.disableBlend();
    }

    public static void drawBackground(Vec3d pos, double offX, double offY, double width, double scale, int height, ColorMutable color) {
        MatrixStack matrices = matrixFrom(pos);
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.translate(offX, offY, 0);
        matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
        int halfWidth = (int) (width / 2);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        DrawableHelper.fill(matrices, -halfWidth, 0, (int) (width - halfWidth), height, color.getRGB());
    }

    public static void drawVignette(float threshold, float power) {
        boolean dif = EntityUtil.getFullHealth(mc.player) <= threshold;
        float f = Math.abs((dif ? EntityUtil.getFullHealth(mc.player) / threshold : 1) - 1F) * power;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(0, f, f, 1.0f);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ((IInGameHud) mc.inGameHud).getVignette());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, mc.getWindow().getScaledHeight(), -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(mc.getWindow().getScaledWidth(), 0.0D, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }

    public static MatrixStack matrixFrom(Vec3d pos) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));
        matrices.translate(pos.getX() - camera.getPos().x, pos.getY() - camera.getPos().y, pos.getZ() - camera.getPos().z);
        return matrices;
    }

    public static void drawItem(ItemStack itemStack, int x, int y, double scale, boolean overlay) {
        if (overlay)
            mc.getItemRenderer().renderGuiItemOverlay(mc.textRenderer, itemStack, (int) (x / scale), (int) (y / scale), null);
        mc.getItemRenderer().renderGuiItemIcon(itemStack, (int) (x / scale), (int) (y / scale));
    }

    public static void drawItem(ItemStack item, Vec3d pos, double scale, double offX, double offY) {
        MatrixStack matrices = matrixFrom(pos);

        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));

        matrices.translate(offX, offY, 0);
        matrices.scale((float) scale, (float) scale, 0.001f);

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f));

        mc.getBufferBuilders().getEntityVertexConsumers().draw();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        DiffuseLighting.disableGuiDepthLighting();

        mc.getItemRenderer().renderItem(item, ModelTransformation.Mode.GUI, 0xF000F0, OverlayTexture.DEFAULT_UV, matrices, mc.getBufferBuilders().getEntityVertexConsumers(), 0);
        mc.getBufferBuilders().getEntityVertexConsumers().draw();
        RenderSystem.disableBlend();
    }

    public static Vec3d getInterpolationOffset(Entity e) {
        if (MinecraftClient.getInstance().isPaused()) return Vec3d.ZERO;
        double tickDelta = MinecraftClient.getInstance().getTickDelta();
        return new Vec3d(e.getX() - MathHelper.lerp(tickDelta, e.lastRenderX, e.getX()), e.getY() - MathHelper.lerp(tickDelta, e.lastRenderY, e.getY()), e.getZ() - MathHelper.lerp(tickDelta, e.lastRenderZ, e.getZ()));
    }

    public static Vec3d smoothMovement(Entity e) {
        return e.getPos().subtract(RenderUtil.getInterpolationOffset(e));
    }

    public static Box smoothMovement(Entity e, Box b) {
        return Box.of(RenderUtil.smoothMovement(e), b.getXLength(), b.getYLength(), b.getZLength()).offset(0, e.getHeight() / 2f, 0);
    }

    public static void setup() {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void setup3D() {
        setup();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
    }

    public static void clean() {
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void clean3D() {
        clean();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
    }
}