package me.offeex.bloomware.client.module.modules.interact;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.api.util.InventoryUtil;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

@Module.Register(name = "MiddleClick", description = "Allows you to use different items when you click middle button.", category = Module.Category.INTERACT)
public class MiddleClick extends Module {

    private final SettingBool friends = new SettingBool.Builder("Friend").value(true).setup(this);
    private final SettingEnum mode = new SettingEnum.Builder("Mode").modes("Pearl", "EXP").selected("Pearl").setup(this);
    private boolean isButtonPressed;

    @Override
    public void onTick() {
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS) {
            if(isButtonPressed) return;
            if(friends.getValue() && mc.targetedEntity instanceof PlayerEntity) {

                String target = mc.targetedEntity.getEntityName();
                FriendManager.PersonType type = Bloomware.friendManager.getType(target);

                if (type != FriendManager.PersonType.FRIEND) {
                    CommandManager.addChatMessagef("friend add %s", target);
                    Bloomware.friendManager.addPerson(target, FriendManager.PersonType.FRIEND);
                } else {
                    CommandManager.addChatMessagef("friend remove %s", target);
                    Bloomware.friendManager.removePerson(target);
                }

                isButtonPressed = true;

                return;
            }

            byte itemSlot = InventoryUtil.findItemInHotbar(mode.is("Pearl") ? Items.ENDER_PEARL : Items.EXPERIENCE_BOTTLE);
            int oldSlot;
            if (itemSlot != -1) {
                if(!mode.is("EXP")) isButtonPressed = true;
                oldSlot = mc.player.getInventory().selectedSlot;
                mc.player.getInventory().selectedSlot = itemSlot;
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                mc.player.getInventory().selectedSlot = oldSlot;
            }
        } else  {
            isButtonPressed = false;
        }
    }
}
