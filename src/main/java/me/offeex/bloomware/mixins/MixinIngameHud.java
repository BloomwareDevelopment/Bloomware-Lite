package me.offeex.bloomware.mixins;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventDrawOverlay;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.modules.screen.DamageTint;
import me.offeex.bloomware.client.module.modules.world.NoRender;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.offeex.bloomware.Bloomware.mc;

@Mixin(InGameHud.class)
public abstract class MixinIngameHud {

    @Inject(at = @At(value = "RETURN"), method = "render", cancellable = true)
    public void render(MatrixStack matrixStack, float float_1, CallbackInfo info) {
        EventDrawOverlay event = new EventDrawOverlay(matrixStack);
        Bloomware.EVENTBUS.post(event);
        if (event.isCancelled()) info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderOverlay(Lnet/minecraft/util/Identifier;F)V", cancellable = true)
    private void onRenderOverlay(Identifier identifier, float scale, CallbackInfo ci) {
        NoRender module = Bloomware.moduleManager.getModule(NoRender.class);
        if (module.isEnabled()) {
            if (identifier.getPath().equals("textures/chat/pumpkinblur.png") && module.pumpkin.getValue()) ci.cancel();
            if (identifier.getPath().equals("textures/chat/vignette.png") && module.vignette.getValue()) ci.cancel();
            if (identifier.getPath().equals("textures/chat/widgets.png") && module.widgets.getValue()) ci.cancel();
            if (identifier.getPath().equals("textures/chat/spyglass_scope.png") && module.spyGlassScope.getValue())
                ci.cancel();
            if (identifier.getPath().equals("textures/chat/powder_snow_outline.png") && module.powderSnowOutline.getValue())
                ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;interactionManager:Lnet/minecraft/client/network/ClientPlayerInteractionManager;", ordinal = 0))
    private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (mc.player == null && mc.interactionManager == null) return;

        DamageTint module = Bloomware.moduleManager.getModule(DamageTint.class);
        float threshold = (float) module.health.getValue();
        float power = (float) module.power.getValue();
        if (mc.interactionManager.getCurrentGameMode().isSurvivalLike() && module.isEnabled())
            RenderUtil.drawVignette(threshold, power);
    }

	@Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
	private void onRenderScoreboard(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective, CallbackInfo ci) {
		NoRender module = Bloomware.moduleManager.getModule(NoRender.class);
		if (module.isEnabled() && module.scoreboard.getValue()) ci.cancel();
	}
}