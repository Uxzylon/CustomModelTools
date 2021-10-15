package com.gmail.anthony17j.model3dplacer.Commands.subcommands;

import com.gmail.anthony17j.model3dplacer.Commands.SubCommand;
import com.gmail.anthony17j.model3dplacer.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class remove3dModel extends SubCommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Supprime votre selection";
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
        ArmorStand stand = Utils.selectedStand.get(uuid);
        if (stand != null) {
            if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                player.sendMessage(ChatColor.YELLOW + "Armor Stand avec CustomModelData " + ChatColor.RED + Objects.requireNonNull(stand.getEquipment().getItemInMainHand().getItemMeta()).getCustomModelData() + ChatColor.YELLOW + " supprimé !");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Armor Stand avec CustomModelData " + ChatColor.RED + Objects.requireNonNull(stand.getEquipment().getHelmet().getItemMeta()).getCustomModelData() + ChatColor.YELLOW + " supprimé !");
            }
            stand.remove();
            Utils.selectedStand.remove(uuid);
        } else {
            player.sendMessage(Utils.noSelection);
        }
    }
}
