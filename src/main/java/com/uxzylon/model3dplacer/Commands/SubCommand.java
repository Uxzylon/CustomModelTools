package com.uxzylon.model3dplacer.Commands;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract String permission();
    public abstract boolean canRunConsole();
    public abstract List<String> getSubcommandArguments(Player player, String[] args);
    public abstract void perform(Player player, String[] args);

    public int getArmorStandCustomModelData(ArmorStand stand) {
        EntityEquipment equipment = stand.getEquipment();
        if (equipment == null) {
            return -1;
        }
        ItemMeta handMeta = equipment.getItemInMainHand().getItemMeta();
        if (handMeta != null) {
            return handMeta.getCustomModelData();
        } else {
            ItemStack helmet = stand.getEquipment().getHelmet();
            if (helmet == null) {
                return -1;
            }
            ItemMeta helmetMeta = helmet.getItemMeta();
            if (helmetMeta == null) {
                return -1;
            }
            return helmetMeta.getCustomModelData();
        }
    }
}
