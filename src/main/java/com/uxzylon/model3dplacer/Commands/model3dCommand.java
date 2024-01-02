package com.uxzylon.model3dplacer.Commands;

import com.uxzylon.model3dplacer.Commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.uxzylon.model3dplacer.Model3DPlacer.plugin;
import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;

public class model3dCommand implements TabExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    public model3dCommand() {
        subCommands.add(new give3dModel());
        subCommands.add(new move3dModel());
        subCommands.add(new place3dModel());
        subCommands.add(new remove3dModel());
        subCommands.add(new select3dModel());
        subCommands.add(new unselect3dModel());
        subCommands.add(new switch3dModel());
        subCommands.add(new reload());
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    public void help(Player player) {
        player.sendMessage(Texts.Title.getText());
        for (int i=0; i < getSubCommands().size(); i++) {
            if (player.hasPermission(getSubCommands().get(i).permission())) {
                player.sendMessage(getSubCommands().get(i).getSyntax() + " - " + ChatColor.GRAY + getSubCommands().get(i).getDescription());
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                boolean found = false;
                for (int i=0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        found = true;
                        if (player.hasPermission(getSubCommands().get(i).permission())) {
                            getSubCommands().get(i).perform(player, args);
                        } else {
                            player.sendMessage(Texts.noPermission.getText());
                        }
                    }
                }
                if (!found) {
                    help(player);
                }
            } else {
                help(player);
            }
        } else {
            if (args.length > 0) {
                for (int i=0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        if (getSubCommands().get(i).canRunConsole()) {
                            getSubCommands().get(i).perform(null, args);
                        } else {
                            plugin.getLogger().info(Texts.playerOnly.getText());
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            ArrayList<String> subCommandsArguments = new ArrayList<>();
            for (int i=0; i < getSubCommands().size(); i++) {
                if (player.hasPermission(getSubCommands().get(i).permission())) {
                    subCommandsArguments.add(getSubCommands().get(i).getName());
                }
            }
            return subCommandsArguments;
        } else if (args.length >= 2) {
            for (int i=0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                    return getSubCommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }
        return null;
    }
}
