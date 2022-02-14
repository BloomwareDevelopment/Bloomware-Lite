package me.offeex.bloomware.client.module.modules.screen;

import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import net.minecraft.world.GameMode;

@Module.Register(name = "FakeGamemode", description = "Changes your client-side gamemode", category = Module.Category.CAMERA)
public class FakeGamemode extends Module {
    private final SettingEnum gamemode = new SettingEnum.Builder("Mode").modes("Survival", "Creative", "Adventure", "Spectator").selected("Creative").setup(this);
    private GameMode oldGamemode = GameMode.SURVIVAL;

    @Override
    public void onEnable() {
        oldGamemode = mc.interactionManager.getCurrentGameMode();
        switch (gamemode.getSelectedStr()) {
            case "Survival" -> mc.interactionManager.setGameMode(GameMode.SURVIVAL);
            case "Creative" -> mc.interactionManager.setGameMode(GameMode.CREATIVE);
            case "Adventure" -> mc.interactionManager.setGameMode(GameMode.ADVENTURE);
            case "Spectator" -> mc.interactionManager.setGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void onDisable() {
        mc.interactionManager.setGameMode(oldGamemode);
    }
}
