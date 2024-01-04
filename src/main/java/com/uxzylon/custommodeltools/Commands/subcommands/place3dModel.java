package com.uxzylon.custommodeltools.Commands.subcommands;

import com.uxzylon.custommodeltools.Commands.SubCommand;
import com.uxzylon.custommodeltools.CustomModelTools;
import com.uxzylon.custommodeltools.ResourcePack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.uxzylon.custommodeltools.CustomModelTools.Texts;
import static com.uxzylon.custommodeltools.CustomModelTools.resourcePack;
import static com.uxzylon.custommodeltools.ResourcePack.guisCategories;

public class place3dModel extends SubCommand {

    @Override
    public String getName() {
        return "place";
    }

    @Override
    public String getDescription() {
        return Texts.placeDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d place <category> <model>";
    }

    @Override
    public String permission() {
        return "custommodeltools.command.place";
    }

    @Override
    public boolean canRunConsole() {
        return false;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return resourcePack.getArgsCategoryModel(args);
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 2 && args[1] != null && args[2] != null) {
            ItemStack item = resourcePack.getItemFromCategoryModel(args[1], args[2]);
            if (item == null) {
                player.sendMessage(String.format(CustomModelTools.Texts.notFoundModel.getText(), getSyntax()));
                return;
            }
            placeModel(player, args[1], args[2], item);
        } else if (args.length == 1) {
            resourcePack.setPlayerFunction(player, ResourcePack.PlayerFunction.PLACE);
            player.openInventory(guisCategories.get(0));
        } else {
            player.sendMessage(String.format(Texts.wrongSyntax.getText(), getSyntax()));
        }
    }
}
