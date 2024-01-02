package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;
import static com.uxzylon.model3dplacer.Model3DPlacer.selectedStand;

public class remove3dModel extends SubCommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return Texts.removeDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d remove";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.remove";
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

            int customModelData = getArmorStandCustomModelData(stand);
            if (customModelData != -1) {
                player.sendMessage(getConfirmMessage(customModelData, stand) + Texts.removed.getText());
            }

            stand.remove();
            selectedStand.remove(uuid);
        } else {
            player.sendMessage(Texts.noSelection.getText());
        }
    }
}
