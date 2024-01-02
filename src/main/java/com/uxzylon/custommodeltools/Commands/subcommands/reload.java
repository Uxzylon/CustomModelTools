package com.uxzylon.custommodeltools.Commands.subcommands;

import com.uxzylon.custommodeltools.Commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.uxzylon.custommodeltools.CustomModelTools.Texts;
import static com.uxzylon.custommodeltools.CustomModelTools.plugin;
import static com.uxzylon.custommodeltools.CustomModelTools.resourcePack;

public class reload extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return Texts.reloadDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d reload";
    }

    @Override
    public String permission() {
        return "custommodeltools.command.reload";
    }

    @Override
    public boolean canRunConsole() {
        return true;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void perform(Player player, String[] args) {
        plugin.reloadConfig();

        resourcePack.updatePack();
        for (Player p : Bukkit.getOnlinePlayers()) {
            resourcePack.setResourcePack(p);
            p.sendMessage(Texts.reloadedPack.getText());
        }
        plugin.getLogger().info(Texts.reloadedPack.getText());
    }
}
