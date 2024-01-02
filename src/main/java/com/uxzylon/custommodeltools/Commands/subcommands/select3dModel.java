package com.uxzylon.custommodeltools.Commands.subcommands;

import com.uxzylon.custommodeltools.Commands.SubCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.uxzylon.custommodeltools.CustomModelTools.Texts;
import static com.uxzylon.custommodeltools.CustomModelTools.selectedStand;

public class select3dModel extends SubCommand {

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public String getDescription() {
        return Texts.selectDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d select";
    }

    @Override
    public String permission() {
        return "custommodeltools.command.select";
    }

    @Override
    public boolean canRunConsole() {
        return false;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void perform(Player player, String[] args) {
        ArmorStand stand = getClosestModel(player);

        if (stand != null) {
            UUID uuid = player.getUniqueId();
            ArmorStand oldStand = selectedStand.get(uuid);
            if (oldStand != null) {
                oldStand.setGlowing(false);
            }
            selectedStand.put(uuid, stand);
            stand.setGlowing(true);

            int customModelData = getArmorStandCustomModelData(stand);
            if (customModelData == -1) {
                return;
            }

            player.sendMessage(getConfirmMessage(customModelData, stand) + Texts.selected.getText());
        } else {
            player.sendMessage(Texts.noCustomModelDataFound.getText());
        }
    }
}
