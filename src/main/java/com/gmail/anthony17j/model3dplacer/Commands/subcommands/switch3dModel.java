package com.gmail.anthony17j.model3dplacer.Commands.subcommands;

import com.gmail.anthony17j.model3dplacer.Commands.SubCommand;
import com.gmail.anthony17j.model3dplacer.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class switch3dModel extends SubCommand {

    @Override
    public String getName() {
        return "switch";
    }

    @Override
    public String getDescription() {
        return "Alterne entre Slot Main et Slot Tête";
    }

    @Override
    public String getSyntax() {
        return "/model3d switch";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.switch";
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
        ArmorStand stand = Utils.selectedStand.get(player.getUniqueId());
        if (stand != null) {
            ItemStack itemHand = stand.getEquipment().getItemInMainHand();
            ItemStack itemHead = stand.getEquipment().getHelmet();
            if (itemHand.getItemMeta() != null) {
                stand.getEquipment().setHelmet(itemHand);
                stand.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
            } else {
                stand.getEquipment().setItemInMainHand(itemHead);
                stand.getEquipment().setHelmet(new ItemStack(Material.AIR));
            }
        } else {
            player.sendMessage(Utils.noSelection);
        }
    }
}
