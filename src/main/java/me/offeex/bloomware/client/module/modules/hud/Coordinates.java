package me.offeex.bloomware.client.module.modules.hud;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.modules.extension.StreamerMode;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

@Module.Register(name = "Coordinates", description = "Shows current coords", category = Module.Category.HUD)
public class Coordinates extends Module {
    private final SettingBool otherDimension = new SettingBool.Builder("OtherDimension").value(true).setup(this);

    String stringXN, stringZN;

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        StreamerMode module = (StreamerMode) Bloomware.moduleManager.getModule("StreamerMode");
        if (!module.isEnabled() || (module.isEnabled() && !module.hideCoords.getValue())) {
            String stringX = String.format("%.1f", MinecraftClient.getInstance().player.getX()).replace(",", ".");
            String stringY = String.format("%.1f", MinecraftClient.getInstance().player.getY()).replace(",", ".");
            String stringZ = String.format("%.1f", MinecraftClient.getInstance().player.getZ()).replace(",", ".");

            switch (getDimension()) {
                case 1 -> {
                    stringXN = String.format("%.1f", MinecraftClient.getInstance().player.getX() * 8).replace(",", ".");
                    stringZN = String.format("%.1f", MinecraftClient.getInstance().player.getZ() * 8).replace(",", ".");
                }
                case 0 -> {
                    stringXN = String.format("%.1f", MinecraftClient.getInstance().player.getX() / 8).replace(",", ".");
                    stringZN = String.format("%.1f", MinecraftClient.getInstance().player.getZ() / 8).replace(",", ".");
                }
            }

            if (getDimension() == 2 || !otherDimension.getValue())
                width = (int) Bloomware.Font.getStringWidth("XYZ: " + stringX + ", " + stringY + ", " + stringZ) + 6;
            else {
                width = (int) Bloomware.Font.getStringWidth("XYZ: " + stringX + ", " + stringY + ", " + stringZ + " (" + stringXN + ", " + stringZN + ")") + 6;
            }

            if (getDimension() == 2 || !otherDimension.getValue())
                Bloomware.Font.drawString(stack, "XYZ: " + stringX + ", " + stringY + ", " + stringZ, x + 3, y + 2, ColorUtils.getHudColor());
            else {
                Bloomware.Font.drawString(stack, "XYZ: " + stringX + ", " + stringY + ", " + stringZ + " (" + stringXN + ", " + stringZN + ")", x + 3, y + 2, ColorUtils.getHudColor());
            }
        }
    }

    public static int getDimension() {
        return switch (MinecraftClient.getInstance().world.getRegistryKey().getValue().getPath()) {
            case "the_nether" -> 1;
            case "the_end" -> 2;
            default -> 0;
        };
    }
}
