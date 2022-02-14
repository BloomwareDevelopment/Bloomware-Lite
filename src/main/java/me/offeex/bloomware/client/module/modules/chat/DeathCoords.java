package me.offeex.bloomware.client.module.modules.chat;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventOpenScreen;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.hud.Coordinates;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;

@Module.Register(name = "DeathCoords", description = "Shows your death coords.", category = Module.Category.CHAT)
public class DeathCoords extends Module {
    private final SettingBool dimension = new SettingBool.Builder("Dimension").value(true).setup(this);

    @Subscribe
    private void onOpenScreen(EventOpenScreen event) {
        if (event.getScreen() instanceof DeathScreen) {
            if (dimension.getValue()) CommandManager.addChatMessage("You died at " + String.format("%.1f", MinecraftClient.getInstance().player.getX())  + " " + String.format("%.1f", MinecraftClient.getInstance().player.getY()) + " " + String.format("%.1f", MinecraftClient.getInstance().player.getZ()) + " in " + convert(Coordinates.getDimension()));
            else CommandManager.addChatMessage("You died at " + String.format("%.1f", MinecraftClient.getInstance().player.getX()));
        }
    }

    private String convert(int dimension) {
        return switch (dimension) {
            case 0 -> "OVERWORLD";
            case 1 -> "NETHER";
            case 2 -> "END";
            default -> "UNKNOWN";
        };
    }
}
