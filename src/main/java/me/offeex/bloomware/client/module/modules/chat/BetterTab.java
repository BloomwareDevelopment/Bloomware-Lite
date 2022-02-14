package me.offeex.bloomware.client.module.modules.chat;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.FriendManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Module.Register(name = "BetterTab", description = "Improves your tab.", category = Module.Category.CHAT)
public class BetterTab extends Module {
    private final SettingGroup size = new SettingGroup.Builder("Size").setup(this);
    private final SettingGroup highlighting = new SettingGroup.Builder("Highlighting").setup(this);
    public final SettingBool modifySize = new SettingBool.Builder("ModifySize").value(true).setup(size);
    private final SettingBool highlightFriends = new SettingBool.Builder("Friends").value(true).setup(highlighting);
    private final SettingBool selfHighlight = new SettingBool.Builder("Self").value(true).setup(highlighting);
    private final SettingBool highlightEnemies = new SettingBool.Builder("Enemies").value(true).setup(highlighting);
    public final SettingNumber count = new SettingNumber.Builder("MaxPlayers").value(80).min(80).max(1000).inc(1).setup(size);
    private final SettingBool hideMe = new SettingBool.Builder("NameProtect").value(false).setup(this);
    public final SettingBool ping = new SettingBool.Builder("Ping").value(true).setup(this);
    public final SettingBool customFont = new SettingBool.Builder("CustomFont").value(false).setup(this);

    public Text getText(PlayerListEntry playerListEntry) {
        String color = null;

        Text name = playerListEntry.getDisplayName();
        if (name == null) name = new LiteralText(playerListEntry.getProfile().getName());

        if (playerListEntry.getProfile().getId().toString().equals(mc.player.getGameProfile().getId().toString())) {
            if (hideMe.getValue()) name = Text.of("Me");
            if (selfHighlight.getValue()) color = "AQUA";
        } else {
            if (Bloomware.friendManager.getType(playerListEntry.getProfile().getName()) != null) {
                if (Bloomware.friendManager.getType(playerListEntry.getProfile().getName()) == FriendManager.PersonType.FRIEND && highlightFriends.getValue()) color = "GREEN";
                if (Bloomware.friendManager.getType(playerListEntry.getProfile().getName()) == FriendManager.PersonType.ENEMY && highlightEnemies.getValue()) color = "RED";
            }
        }

        if (color != null) {
            String nameString = name.getString();
            for (Formatting format : Formatting.values()) {
                if (format.isColor()) nameString = nameString.replace(format.toString(), "");
            }
            name = new LiteralText(nameString).setStyle(name.getStyle().withColor(Formatting.byName(color)));
        }
        return name;
    }
}
