package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;

public class give3dModel extends SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return Texts.giveDescription.getText();
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
        if (args.length > 1 && isInt(args[1])) {
            ItemStack item = new ItemStack(Material.CLOCK);
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return;
            }
            meta.setCustomModelData(Integer.valueOf(args[1]));
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(Texts.needId.getText());
        }
    }
}
