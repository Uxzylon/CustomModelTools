package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;
import static com.uxzylon.model3dplacer.Model3DPlacer.customModelDatas;

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
        return "/model3d give <category> <model>";
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
        return getArgsCategoryModel(args);
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 2 && args[1] != null && args[2] != null) {

            ItemStack item = getItemFromCategoryModel(args[1], args[2]);
            if (item == null) {
                player.sendMessage(String.format(Texts.notFoundModel.getText(), getSyntax()));
                return;
            }

            player.getInventory().addItem(item);

            Pair<Material, Integer> material = customModelDatas.get(args[1]).get(args[2]);
            player.sendMessage(String.format(Texts.given.getText(), args[2], args[1], material.getLeft(), material.getRight()));
        } else {
            player.sendMessage(String.format(Texts.wrongSyntax.getText(), getSyntax()));
        }
    }
}
