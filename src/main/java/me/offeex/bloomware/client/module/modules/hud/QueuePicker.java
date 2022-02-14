package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.util.math.MatrixStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Module.Register(name = "QueuePicker", description = "Shows current priority queue state", category = Module.Category.HUD)
public class QueuePicker extends Module {
    private final SettingNumber delay = new SettingNumber.Builder("Delay").min(2).max(120).value(15).inc(1).setup(this);

    private String a = "Queue: API is unreachable!";

    private final Thread thread = new Thread(() -> {
        InputStream connection;
        while (!Thread.currentThread().isInterrupted() && this.isEnabled()) {
            try {
                connection = new URL("https://api.2b2t.dev/prioq").openStream();
                a = parse(new BufferedReader(new InputStreamReader(connection, StandardCharsets.UTF_8)).readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep((long) delay.getValue() * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    private String parse(String text) {
        String[] args = text.split(",");
        return args[1] + " people, est. time: " + args[2].substring(1, args[2].length() - 2);
    }

    @Override
    public void onEnable() {
        thread.start();
    }

    @Override
    public void onDisable() {
        thread.interrupt();
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        width = (int) Bloomware.Font.getStringWidth("Queue: " + a) + 6;
        Bloomware.Font.drawString(stack, "Queue: " + a, x + 3, y + 2, ColorUtils.getHudColor());
    }
}
