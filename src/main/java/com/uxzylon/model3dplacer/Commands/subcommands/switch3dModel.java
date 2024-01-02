package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;
import static com.uxzylon.model3dplacer.Model3DPlacer.selectedStand;

public class switch3dModel extends SubCommand {

    @Override
    public String getName() {
        return "switch";
    }

    @Override
    public String getDescription() {
        return Texts.switchDescription.getText();
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
        ArmorStand stand = selectedStand.get(player.getUniqueId());
        if (stand != null) {
            EntityEquipment equipment = stand.getEquipment();
            if (equipment == null) {
                return;
            }
            ItemStack itemHand = equipment.getItemInMainHand();
            ItemStack itemHead = stand.getEquipment().getHelmet();
            String slot = "Hand";
            if (itemHand.getItemMeta() != null) {
                stand.getEquipment().setHelmet(itemHand);
                stand.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                slot = "Head";
            } else {
                stand.getEquipment().setItemInMainHand(itemHead);
                stand.getEquipment().setHelmet(new ItemStack(Material.AIR));
            }

            int customModelData = getArmorStandCustomModelData(stand);
            player.sendMessage(getConfirmMessage(customModelData, stand) + String.format(Texts.switched.getText(), slot));
        } else {
            player.sendMessage(Texts.noSelection.getText());
        }
    }
}
