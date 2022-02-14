package me.offeex.bloomware.client.module.modules.visuals;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventWorldRender;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.api.util.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingColor;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;

@Module.Register(name = "ESP", description = "Highlights various things through blocks", category = Module.Category.VISUALS)
public class ESP extends Module {
    private final SettingGroup animals = new SettingGroup.Builder("Animals").toggleable(true).setup(this);
    private final SettingColor animalsColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 255, 0, 255)).setup(animals);
    private final SettingEnum animalsMode = new SettingEnum.Builder("Mode").modes("Fill", "Box").selected("Box").setup(animals);

    private final SettingGroup hostiles = new SettingGroup.Builder("Hostiles").toggleable(true).setup(this);
    private final SettingColor hostilesColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 255, 0, 255)).setup(hostiles);
    private final SettingEnum hostilesMode = new SettingEnum.Builder("Mode").modes("Fill", "Box").selected("Box").setup(hostiles);

    private final SettingGroup players = new SettingGroup.Builder("Players").toggleable(true).setup(this);
    private final SettingColor playersColor = new SettingColor.Builder("Color").color(new ColorMutable(0, 255, 0, 255)).setup(players);
    private final SettingEnum playersMode = new SettingEnum.Builder("Mode").modes("Fill", "Box").selected("Box").setup(players);

    private final SettingColor enemiesColor = new SettingColor.Builder("EnemiesColor").color(new ColorMutable(255, 0, 0, 255)).setup(players);
    private final SettingColor friendsColor = new SettingColor.Builder("FriendsColor").color(new ColorMutable(0, 255, 0, 255)).setup(players);

    private final SettingGroup items = new SettingGroup.Builder("Items").toggleable(true).setup(this);
    private final SettingEnum itemsMode = new SettingEnum.Builder("Mode").modes("Fill", "Box").selected("Box").setup(items);
    private final SettingColor itemsColor = new SettingColor.Builder("Color").color(new ColorMutable(255, 255, 255, 255)).setup(items);

    private final SettingNumber range = new SettingNumber.Builder("Range").min(1).max(300).inc(1).value(200).setup(this);
    private final SettingNumber lineWidth = new SettingNumber.Builder("Width").min(1).max(5).inc(0.1).value(4).setup(this);

    private final ArrayList<Entity> entities = new ArrayList<>();

    private void selectEntities() {
        entities.clear();
        mc.world.getEntities().forEach(entity -> {
            if (mc.player.distanceTo(entity) <= range.getValue())
                entities.add(entity);
        });
    }

    @Override
    public void onDisable() {
        entities.clear();
    }

    @Subscribe
    private void onWorldRender(EventWorldRender event) {
        selectEntities();
        for (Entity e : entities) {
            Box box = RenderUtil.smoothMovement(e, e.getBoundingBox());
            if (e instanceof PlayerEntity && players.isToggled() && e != mc.player) {
                if (Bloomware.friendManager.getType(e.getEntityName()) != null) {
                    FriendManager.PersonType type = Bloomware.friendManager.getType(e.getEntityName());
                    if (playersMode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, box, type == FriendManager.PersonType.FRIEND ? friendsColor.getColor() : enemiesColor.getColor());
                    else RenderUtil.drawOutline(event.matrixStack, box, type == FriendManager.PersonType.FRIEND ? friendsColor.getColor() : enemiesColor.getColor(), lineWidth.getValue());
                } else {
                    if (playersMode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, box, playersColor.getColor());
                    else RenderUtil.drawOutline(event.matrixStack, box, playersColor.getColor(), lineWidth.getValue());
                }
            } else if (e instanceof Monster && hostiles.isToggled()) {
                if (hostilesMode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, box, hostilesColor.getColor());
                else RenderUtil.drawOutline(event.matrixStack, box, hostilesColor.getColor(), lineWidth.getValue());
            } else if ((e instanceof PassiveEntity || e instanceof WaterCreatureEntity || e instanceof AmbientEntity || e instanceof SnowGolemEntity || e instanceof IronGolemEntity) && animals.isToggled()) {
                if (animalsMode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, box, animalsColor.getColor());
                else RenderUtil.drawOutline(event.matrixStack, box, animalsColor.getColor(), lineWidth.getValue());
            } else if (e instanceof ItemEntity && items.isToggled()) {
                if (itemsMode.is("Fill")) RenderUtil.drawFilledBox(event.matrixStack, box, itemsColor.getColor());
                else RenderUtil.drawOutline(event.matrixStack, box, itemsColor.getColor(), lineWidth.getValue());
            }
        }
    }
}
