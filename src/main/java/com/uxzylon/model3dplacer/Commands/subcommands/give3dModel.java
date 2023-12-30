package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import com.uxzylon.model3dplacer.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class give3dModel extends SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Obtenez un CustomModelData dans votre inventaire";
    }

    @Override
    public String getSyntax() {
        return "/model3d give <id>";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.give";
    }

    @Override
    public boolean canRunConsole() {
        return false;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2) {
            return Collections.singletonList("<id>");
        }
        return Collections.emptyList();
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1 && Utils.isInt(args[1])) {
            ItemStack item = new ItemStack(Material.CLOCK);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setCustomModelData(Integer.valueOf(args[1]));
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(Utils.needId);
        }
    }
}
