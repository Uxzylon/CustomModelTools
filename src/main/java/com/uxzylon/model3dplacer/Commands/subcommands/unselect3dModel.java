package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import com.uxzylon.model3dplacer.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class unselect3dModel extends SubCommand {

    @Override
    public String getName() {
        return "unselect";
    }

    @Override
    public String getDescription() {
        return "Déselectionne votre sélection";
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
        ArmorStand stand = Utils.selectedStand.get(uuid);
        if (stand != null) {
            stand.setGlowing(false);
            Utils.selectedStand.remove(uuid);
            if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                player.sendMessage(ChatColor.YELLOW + "Armor Stand avec CustomModelData " + ChatColor.GREEN + Objects.requireNonNull(stand.getEquipment().getItemInMainHand().getItemMeta()).getCustomModelData() + ChatColor.YELLOW + " désélectionné !");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Armor Stand avec CustomModelData " + ChatColor.GREEN + Objects.requireNonNull(stand.getEquipment().getHelmet().getItemMeta()).getCustomModelData() + ChatColor.YELLOW + " désélectionné !");
            }
        } else {
            player.sendMessage(Utils.noSelection);
        }
    }
}
