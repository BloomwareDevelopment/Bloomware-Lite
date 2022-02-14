package me.offeex.bloomware.client.module.modules.world;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventPortalGUI;
import me.offeex.bloomware.api.event.events.EventRenderEntity;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.FallingBlockEntity;

@Module.Register(name = "NoRender", description = "Allows you to cancel useless things rendering", category = Module.Category.WORLD)
public class NoRender extends Module {
    private final SettingGroup overlay = new SettingGroup.Builder("Overlay").setup(this);
    private final SettingGroup game = new SettingGroup.Builder("Game").setup(this);
    private final SettingGroup particles = new SettingGroup.Builder("Particles").setup(this);

    public final SettingBool pumpkin = new SettingBool.Builder("Pumpkin").setup(overlay);
    public final SettingBool vignette = new SettingBool.Builder("Vignette").setup(overlay);
    public final SettingBool widgets = new SettingBool.Builder("Widgets").setup(overlay);
    public final SettingBool spyGlassScope = new SettingBool.Builder("SpyGlassScope").setup(overlay);
    public final SettingBool powderSnowOutline = new SettingBool.Builder("PowderSnow").setup(overlay);
    public final SettingBool fire = new SettingBool.Builder("Fire").setup(overlay);
    public final SettingBool water = new SettingBool.Builder("Water").setup(overlay);
    public final SettingBool walls = new SettingBool.Builder("Walls").setup(overlay);
    public final SettingBool nausea = new SettingBool.Builder("Nausea").setup(overlay);

    public final SettingBool enchantedTable = new SettingBool.Builder("EnchantedTable").setup(game);
    public final SettingBool bob = new SettingBool.Builder("Bob").setup(game);
    public final SettingBool hurtCamera = new SettingBool.Builder("HurtCamera").setup(game);
    public final SettingBool fireworks = new SettingBool.Builder("Fireworks").setup(game);
    public final SettingBool fog = new SettingBool.Builder("Fog").setup(game);
    private final SettingBool fallingBlocks = new SettingBool.Builder("FallingBlocks").value(true).setup(game);
    private final SettingBool experienceOrbs = new SettingBool.Builder("ExperienceOrb").value(false).setup(game);
    public final SettingBool scoreboard = new SettingBool.Builder("Scoreboard").setup(game);
    public final SettingBool weather = new SettingBool.Builder("Weather").setup(game);

    public final SettingBool fireworkDust = new SettingBool.Builder("FireworkDust").setup(particles);
    public final SettingBool explosions = new SettingBool.Builder("Explosions").setup(particles);

    @Subscribe
    private void onCloseGui(EventPortalGUI event) {
        event.setCancelled(true);
    }

    @Subscribe
    private void onEntityRender(EventRenderEntity event) {
        event.setCancelled(event.getEntity() instanceof ExperienceOrbEntity && experienceOrbs.getValue());
        event.setCancelled(event.getEntity() instanceof FallingBlockEntity && fallingBlocks.getValue());
    }

    /**
     * @see "post all shit here"
     */
}
