package me.offeex.bloomware.client.gui.screen.mainmenu;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.mainmenu.widget.MainMenuButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomMainMenu extends Screen {
    private final Identifier BACKGROUND = new Identifier("bloomware", "background/highwaybg.png");
    private final Identifier LOGO = new Identifier("bloomware", "background/bloomware.png");
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final ArrayList<Component> components = new ArrayList<>();

    public CustomMainMenu() {
        super(Text.of("Custom main menu"));
        components.addAll(Arrays.asList(
                new MainMenuButton(10, mc.getWindow().getScaledHeight() / 2 - 62, 170, 124, new SelectWorldScreen(this), new Identifier("bloomware", "gui/mainmenu/singleplayer.png")),
                new MainMenuButton(mc.getWindow().getScaledWidth() - 161, mc.getWindow().getScaledHeight() / 2 - 56, 151, 113, new MultiplayerScreen(this), new Identifier("bloomware", "gui/mainmenu/multiplayer.png"))));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        RenderSystem.setShaderTexture(0, LOGO);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        DrawableHelper.drawTexture(matrices, mc.getWindow().getScaledWidth() / 2 - 237, 20, 0, 0,474, 67, 474, 67);
        components.forEach(component -> component.render(matrices));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
        return false;
    }
}
