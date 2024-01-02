package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;
import static com.uxzylon.model3dplacer.Model3DPlacer.selectedStand;

public class unselect3dModel extends SubCommand {

    @Override
    public String getName() {
        return "unselect";
    }

    @Override
    public String getDescription() {
        return Texts.unselectDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d unselect";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.unselect";
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
        UUID uuid = player.getUniqueId();
        ArmorStand stand = selectedStand.get(uuid);
        if (stand != null) {
            stand.setGlowing(false);
            selectedStand.remove(uuid);

            int customModelData = getArmorStandCustomModelData(stand);
            if (customModelData == -1) {
                return;
            }

            player.sendMessage(getConfirmMessage(customModelData, stand) + Texts.unselected.getText());
        } else {
            player.sendMessage(Texts.noSelection.getText());
        }
    }
}
