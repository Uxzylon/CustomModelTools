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

public class select3dModel extends SubCommand {

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public String getDescription() {
        return "Sélectionne le CustomModelData le plus proche";
    }

    @Override
    public String getSyntax() {
        return "/model3d select";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.select";
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
        ArmorStand stand = Utils.getClosestModel(player);
        if (stand != null) {
            UUID uuid = player.getUniqueId();
            ArmorStand oldStand = Utils.selectedStand.get(uuid);
            if (oldStand != null) {
                oldStand.setGlowing(false);
            }
            Utils.selectedStand.put(uuid, stand);
            stand.setGlowing(true);
            if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                player.sendMessage(ChatColor.YELLOW + "Armor Stand avec CustomModelData " + ChatColor.GREEN + Objects.requireNonNull(stand.getEquipment().getItemInMainHand().getItemMeta()).getCustomModelData() + ChatColor.YELLOW + " sélectionné !");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Armor Stand avec CustomModelData " + ChatColor.GREEN + Objects.requireNonNull(stand.getEquipment().getHelmet().getItemMeta()).getCustomModelData() + ChatColor.YELLOW + " sélectionné !");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Aucun Armor Stand avec CustomModelData trouvé !");
        }
    }
}
