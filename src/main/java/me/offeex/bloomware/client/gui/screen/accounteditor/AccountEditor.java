package me.offeex.bloomware.client.gui.screen.accounteditor;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.alts.Alt;
import me.offeex.bloomware.client.gui.screen.accounteditor.component.PictureWindow;
import me.offeex.bloomware.client.gui.screen.accounteditor.component.components.altmanager.AltWidget;
import me.offeex.bloomware.client.gui.screen.accounteditor.component.components.altmanager.AltsWindow;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import static me.offeex.bloomware.Bloomware.mc;

import java.util.ArrayList;
import java.util.Arrays;

public class AccountEditor extends Screen {
    private final ArrayList<PictureWindow> windows = new ArrayList<>();

    public static final Identifier ALT_FIELD = new Identifier("bloomware", "elements/gui/accounts/altmanager/cell.png");
    public static final Identifier ALT_OUTLINE = new Identifier("bloomware", "elements/gui/accounts/altmanager/cell_outline.png");
    public static final Identifier ALT_SELECTED_OUTLINE = new Identifier("bloomware", "elements/gui/accounts/altmanager/cell_selected.png");
    public static final Identifier EDIT_BUTTON = new Identifier("bloomware", "elements/gui/accounts/altmanager/edit.png");
    public static final Identifier LOGIN_BUTTON = new Identifier("bloomware", "elements/gui/accounts/altmanager/login.png");

    private final ArrayList<Component> alts = new ArrayList<>(Arrays.asList(
            new AltWidget(new Alt("loh", "pidor", Alt.AltType.CRACKED)),
            new AltWidget(new Alt("loh123", "pidor", Alt.AltType.CRACKED))
    ));

    public AccountEditor() {
        super(new LiteralText("Account Editor"));
        Identifier FILL_TEXTURE = new Identifier("bloomware", "elements/gui/accounts/bigframe.png");
        windows.add(new AltsWindow(alts, FILL_TEXTURE, 10, 10, 339, 355));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        windows.forEach(window -> window.render(matrices, mouseX, mouseY, partialTicks));

        Bloomware.hud.getToolBar().updateProperties(0, mc.getWindow().getScaledHeight() - 25, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        Bloomware.hud.getToolBar().render(matrices);
        Bloomware.hud.getToolBar().getComponents().forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        Bloomware.hud.getToolBar().getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        Bloomware.hud.getToolBar().getComponents().forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Bloomware.hud.getToolBar().getComponents().forEach(c -> c.keyTyped(keyCode));
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) mc.setScreen(null);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
