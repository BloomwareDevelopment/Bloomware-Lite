package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.HoleUtil;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.api.util.WorldUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;

@Module.Register(name = "HoleESP", description = "Renders safe holes", category = Module.Category.VISUALS)
public class HoleESP extends Module {
    private final SettingGroup obsidianHoles = new SettingGroup.Builder("ObsidianHoles").toggleable(true).toggled(true).setup(this);
    private final SettingEnum obsidianMode = new SettingEnum.Builder("Mode").modes("Fill3D", "Outline3D", "Fill2D", "Outline2D").selected("Outline2D").setup(obsidianHoles);
    private final SettingNumber obsidianWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(obsidianHoles);
    private final SettingColor obsidianColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 189, 252, 255)).setup(obsidianHoles);

    private final SettingGroup bedrockHoles = new SettingGroup.Builder("BedrockHoles").toggleable(true).toggled(true).setup(this);
    private final SettingEnum bedrockMode = new SettingEnum.Builder("Mode").modes("Fill3D", "Outline3D", "Fill2D", "Outline2D").selected("Outline2D").setup(bedrockHoles);
    private final SettingNumber bedrockWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(bedrockHoles);
    private final SettingColor bedrockColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 189, 252, 255)).setup(bedrockHoles);

    private final SettingNumber range = new SettingNumber.Builder("Range").min(5).max(30).inc(1).value(10).setup(this);
    private final ArrayList<HoleUtil.Hole> holes = new ArrayList<>();

    @Override
    public void onDisable() {
        holes.clear();
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        WorldUtil.findHoles(holes, range, mc.player);
        holes.forEach(hole -> {
            if (hole.type().equals(HoleUtil.HoleType.BEDROCK) && bedrockHoles.isToggled()) renderHole(event, hole.pos(), bedrockMode, bedrockColor);
            else if (hole.type().equals(HoleUtil.HoleType.SAFE) && obsidianHoles.isToggled()) renderHole(event, hole.pos(), obsidianMode, obsidianColor);
        });
    }

    private void renderHole(EventWorldRender event, BlockPos pos, SettingEnum mode, SettingColor color) {
        switch (mode.getSelectedStr()) {
            case "Fill2D" -> RenderUtil.drawFilledBox(event.matrixStack, new Box(pos, pos.add(1, 0.01, 1)), color.getColor());
            case "Fill3D" -> RenderUtil.drawFilledBox(event.matrixStack, new Box(pos), color.getColor());
            case "Outline2D" -> RenderUtil.drawOutline(event.matrixStack, new Box(pos, pos.add(1, 0.01, 1)), color.getColor(), color == bedrockColor ? bedrockWidth.getValue() : obsidianWidth.getValue());
            case "Outline3D" -> RenderUtil.drawOutline(event.matrixStack, new Box(pos), color.getColor(), color == bedrockColor ? bedrockWidth.getValue() : obsidianWidth.getValue());
            default -> throw new IllegalArgumentException();
        }
    }
}
