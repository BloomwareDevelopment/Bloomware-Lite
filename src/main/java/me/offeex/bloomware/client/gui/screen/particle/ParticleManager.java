package me.offeex.bloomware.client.gui.screen.particle;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.module.modules.client.Gui;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.offeex.bloomware.Bloomware.mc;

public class ParticleManager {
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();
    private final Random random = new Random();

    private void spawn() {
        Gui module = Bloomware.moduleManager.getModule(Gui.class);
        for (int i = 0; i < module.amount.getValue() - particles.size(); i++) {
            Particle particle = generate(module);
            particles.add(particle);
        }
    }

    public void render(MatrixStack stack) {
        Gui module = Bloomware.moduleManager.getModule(Gui.class);
        spawn();
        for (Particle p : particles) {
            p.move();
            p.updateMovement();
            p.draw(stack, module.glow.getValue());
            if (p.getX() > mc.getWindow().getScaledWidth() + 5 || p.getX() < -5 || p.getY() > mc.getWindow().getScaledHeight() + 5 || p.getY() < -5)
                particles.remove(p);
        }
    }

    private Particle generate(Gui m) {
        return new Particle(
                random.nextInt(0, mc.getWindow().getScaledWidth()),
                particles.size() < m.amount.getValue() ? random.nextInt(-mc.getWindow().getScaledHeight(), -5) : -5,
                randomDiff(m.sizeValue, m.sizeDiff),
                randomDiff(m.speedValue, m.speedDiff),
                randomDiff(m.windValue, m.windDiff),
                m.colorMode.is("Static") ? m.colorParticle.getColor() : ColorMutable.random(random));
    }

    private boolean diffCheck(SettingNumber value, SettingNumber diff) {
        return value.getValue() - diff.getValue() > 0 && diff.getValue() != 0;
    }

    private int randomDiff(SettingNumber value, SettingNumber diff) {
        return diffCheck(value, diff) ? random.nextInt((int) (value.getValue() - diff.getValue()), (int) (value.getValue() + diff.getValue())) : (int) value.getValue();
    }
}
