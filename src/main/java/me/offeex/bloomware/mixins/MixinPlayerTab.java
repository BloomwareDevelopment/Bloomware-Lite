package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.client.module.modules.chat.BetterTab;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(PlayerListHud.class)
public class MixinPlayerTab {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 0), index = 1)
    private int modifyCount(int count) {
        BetterTab module = Bloomware.moduleManager.getModule(BetterTab.class);
        return module.isEnabled() && module.modifySize.getValue() ? (int) module.count.getValue() : 80;
    }

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> info) {
        BetterTab module = Bloomware.moduleManager.getModule(BetterTab.class);
        if (module.isEnabled()) info.setReturnValue(module.getText(playerListEntry));
    }

    @Shadow
    protected void renderLatencyIcon(MatrixStack matrices, int width, int x, int y, PlayerListEntry entry) {}

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;renderLatencyIcon(Lnet/minecraft/client/util/math/MatrixStack;IIILnet/minecraft/client/network/PlayerListEntry;)V"))
    protected void renderLatencyIcon(PlayerListHud instance, MatrixStack matrixStack, int i, int j, int k, PlayerListEntry playerListEntry) {
        BetterTab module = Bloomware.moduleManager.getModule(BetterTab.class);
        if (module.isEnabled() && module.ping.getValue()) {
            int ping = playerListEntry.getLatency();
            if (module.customFont.getValue()) Bloomware.Font.drawString(matrixStack, String.valueOf(ping), i + j - Bloomware.Font.getStringWidth(String.valueOf(ping)), k - 3, calculateColor(ping));
            else mc.textRenderer.draw(matrixStack, String.valueOf(ping), i + j - mc.textRenderer.getWidth(String.valueOf(ping)), k, calculateColor(ping).getRGB());
        } else renderLatencyIcon(matrixStack, i, j, k, playerListEntry);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    protected int drawString(TextRenderer instance, MatrixStack matrixStack, Text text, float f, float g, int i) {
        BetterTab module = Bloomware.moduleManager.getModule(BetterTab.class);
        if (module.isEnabled() && module.customFont.getValue()) {
            Bloomware.Font.drawString(matrixStack, text.getString(), f, g - 3, text.getStyle().getColor() == null ? new ColorMutable(-1) : new ColorMutable(text.getStyle().getColor().getRgb()));
        } else {
            mc.textRenderer.draw(matrixStack, text, f, g, i);
        }
        return 0;
    }

    private ColorMutable calculateColor(int ping) {
        return ping < 150 ? ColorMutable.GREEN : ping < 300 ? ColorMutable.YELLOW : ColorMutable.RED;
    }
}
