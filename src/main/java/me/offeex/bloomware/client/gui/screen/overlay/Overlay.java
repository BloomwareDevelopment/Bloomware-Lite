package me.offeex.bloomware.client.gui.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.gui.screen.overlay.musicplayer.component.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Overlay extends Screen {
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss, yyyy.MM.dd");
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public static final ColorMutable BLACK_OPACITY = new ColorMutable(0, 0, 0, 150);
    private final ColorMutable DARK_GRAY = new ColorMutable(29, 29, 29, 255);
    public static final Identifier TRACK_ICON = new Identifier("bloomware", "elements/gui/musicplayer/track.png");
    public static final Identifier FORWARD_ICON = new Identifier("bloomware", "elements/gui/musicplayer/forward_button.png");
    public static final Identifier BACKWARD_ICON = new Identifier("bloomware", "elements/gui/musicplayer/backward_button.png");
    public static final Identifier STOP_ICON = new Identifier("bloomware", "elements/gui/musicplayer/stop_button.png");
    public static final Identifier REPEAT_PLAYBACK_ICON = new Identifier("bloomware", "elements/gui/musicplayer/repeat_playlist_button.png");
    public static final Identifier REPEAT_SONG_ICON = new Identifier("bloomware", "elements/gui/musicplayer/repeat_button.png");
    public static final Identifier SELECT_RANDOM_ICON = new Identifier("bloomware", "elements/gui/musicplayer/random_button.png");
    private final ArrayList<Component> MUSIC_PLAYER_CONTROLS;
    private final ArrayList<Component> TRACKS = new ArrayList<>();
    private int offset = 0;

    public Overlay() {
        super(Text.of("Overlay"));
        MUSIC_PLAYER_CONTROLS = new ArrayList<>(Arrays.asList(
                new SelectFolderButton(),
                new NextSongButton(),
                new PreviousSongButton(),
                new StopMusicButton(),
                new PlaybackButton()));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        fillGradient(matrices, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new ColorMutable(0, 0, 0, 255).getRGB(), ColorMutable.EMPTY.getRGB());
        Bloomware.Font.drawString(matrices, "Welcome to Bloomware client!", 10, 10, ColorMutable.WHITE);
        Bloomware.Font.drawString(matrices, format.format(Calendar.getInstance().getTime()), 30, 25, ColorMutable.WHITE);
        RenderUtil.drawItem(Items.PAPER.getDefaultStack(), 10, 25, 1, true);
        Bloomware.Font.drawString(matrices, String.format("Current session playtime: %s", Bloomware.sessionManager.convertTime(Bloomware.sessionManager.getTimeOnline())), 30, 40, ColorMutable.WHITE);
        RenderUtil.drawItem(Items.CLOCK.getDefaultStack(), 10, 40, 1, true);
        renderMusicPlayer(matrices, mouseX, mouseY, partialTicks);
    }

    private void renderMusicPlayer(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        fill(matrices, 10, 60, mc.getWindow().getScaledWidth() - 200, mc.getWindow().getScaledHeight() - 10, BLACK_OPACITY.getRGB());
        fill(matrices, 10, 60, mc.getWindow().getScaledWidth() - 200, 75, DARK_GRAY.getRGB());
        fill(matrices, 10, mc.getWindow().getScaledHeight() - 60, mc.getWindow().getScaledWidth() - 200, mc.getWindow().getScaledHeight() - 10, DARK_GRAY.getRGB());
        RenderUtil.drawItem(Items.MUSIC_DISC_CAT.getDefaultStack(), 11, 60, 1, false);
        Bloomware.Font.drawString(matrices, "Bloomware Music Player (" + TRACKS.size() + " songs loaded)", 30, 61, ColorMutable.WHITE);
        MUSIC_PLAYER_CONTROLS.forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
        renderTracks(matrices, mouseX, mouseY, partialTicks);
        if (Bloomware.musicManager.getCurrentTrack() != null) {
            drawTexture(TRACK_ICON, 10, mc.getWindow().getScaledHeight() - 60, 50, 50, matrices);
            Bloomware.Font.drawString(matrices, Bloomware.musicManager.getCurrentTrack().getName(), 70, mc.getWindow().getScaledHeight() - 30, ColorMutable.WHITE);
        }
    }

    private void renderTracks(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        if (TRACKS.size() <= 11) {
            TRACKS.forEach(track -> {
                track.render(matrices);
                track.updateComponent(mouseX, mouseY);
            });
        } else {
            int offset_ = 80;
            for (int i = offset; i < 11 + offset; i++) {
                ((SongComponent) TRACKS.get(i)).setOffset(offset_);
                TRACKS.get(i).render(matrices);
                TRACKS.get(i).updateComponent(mouseX, mouseY);
                if (Bloomware.musicManager.getCurrentTrack() != null) {
                    if (Bloomware.musicManager.getCurrentTrack().getName().equals(((SongComponent) TRACKS.get(i)).getTrack().getName())) {
                        Bloomware.Font.drawString(matrices, "Now playing!", 70, offset_ + 20, ColorMutable.GREEN);
                    }
                }
                offset_ += 50;
            }
        }
    }

    public void reloadTracks() {
        TRACKS.clear();
        int offset = 80;
        for (File file : Bloomware.musicManager.getTracks()) {
            SongComponent component = new SongComponent(file);
            component.setOffset(offset);
            TRACKS.add(component);
        }
    }

    public static void drawTexture(Identifier icon, int x, int y, int width, int height, MatrixStack stack) {
        RenderSystem.setShaderTexture(0, icon);
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        DrawableHelper.drawTexture(stack, x, y, 0, 0, width, height, width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHoveredMusicWindow(mouseX, mouseY)) {
            MUSIC_PLAYER_CONTROLS.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
            TRACKS.forEach(track -> track.mouseClicked(mouseX, mouseY, mouseButton));
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        if (isHoveredMusicWindow(mouseX, mouseY)) {
            MUSIC_PLAYER_CONTROLS.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
            TRACKS.forEach(track -> track.mouseReleased(mouseX, mouseY, state));
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) mc.setScreen(null);
        MUSIC_PLAYER_CONTROLS.forEach(component -> component.keyTyped(keyCode));
        TRACKS.forEach(track -> track.keyTyped(keyCode));
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (isHoveredMusicWindow(mouseX, mouseY)) {
            if (amount > 0 && offset > 0) offset--;
            else if (amount < 0 && offset < TRACKS.size() - 11) offset++;
        }
        return true;
    }

    private boolean isHoveredMusicWindow(double mouseX, double mouseY) {
        return mouseX > 15 && mouseX < mc.getWindow().getScaledWidth() - 200 && mouseY > 60 && mouseY < mc.getWindow().getScaledHeight() - 10;
    }
}
